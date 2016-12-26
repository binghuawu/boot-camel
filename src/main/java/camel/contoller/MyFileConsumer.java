package camel.contoller;

import java.io.File;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.file.FileConsumer;
import org.apache.camel.component.file.GenericFile;
import org.apache.camel.component.file.GenericFileEndpoint;
import org.apache.camel.component.file.GenericFileOperations;

public class MyFileConsumer extends FileConsumer {

	public MyFileConsumer(GenericFileEndpoint<File> endpoint, Processor processor,
		GenericFileOperations<File> operations) {
		super(endpoint, processor, operations);
	}

	@Override
	protected boolean isValidFile(GenericFile<File> file, boolean isDirectory, List<File> files) {
		// TODO override the way how to calculate the KEY
		String absoluteFilePath = file.getAbsoluteFilePath();

		if (!isMatched(file, isDirectory, files)) {
			log.trace("File did not match. Will skip this file: {}", file);
			return false;
		}

		// directory is always valid
		if (isDirectory) {
			return true;
		}

		// check if file is already in progress
		if (endpoint.getInProgressRepository().contains(absoluteFilePath)) {
			if (log.isTraceEnabled()) {
				log.trace("Skipping as file is already in progress: {}", file.getFileName());
			}
			return false;
		}

		// if its a file then check we have the file in the idempotent registry already
		if (endpoint.isIdempotent()) {
			// use absolute file path as default key, but evaluate if an expression key was configured
			String key = file.getAbsoluteFilePath();
			if (endpoint.getIdempotentKey() != null) {
				Exchange dummy = endpoint.createExchange(file);
				key = endpoint.getIdempotentKey().evaluate(dummy, String.class);
			}
			// add the timestamp of the parent folder anyway
			key += "_" + file.getFile().getParentFile().lastModified();
			if (key != null && endpoint.getIdempotentRepository().contains(key)) {
				log.trace(
					"This consumer is idempotent and the file has been consumed before matching idempotentKey: {}. Will skip this file: {}",
					key, file);
				return false;
			}
		}

		// okay so final step is to be able to add atomic as in-progress, so we are the
		// only thread processing this file
		return endpoint.getInProgressRepository().add(absoluteFilePath);
	}

}
