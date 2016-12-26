package camel;

import java.io.File;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.file.FileConsumer;
import org.apache.camel.component.file.FileEndpoint;
import org.apache.camel.component.file.GenericFileOperations;
import org.apache.camel.spi.UriEndpoint;

import camel.contoller.MyFileConsumer;
@UriEndpoint(scheme = "argus-file", consumerClass = MyFileConsumer.class)
public class MyFileEndpoint extends FileEndpoint {

	public MyFileEndpoint(String uri, MyFileComponent myFileComponent) {
		super(uri, myFileComponent);
	}

	@Override
	protected FileConsumer newFileConsumer(Processor processor, GenericFileOperations<File> operations) {
		return new MyFileConsumer(this, processor, operations);
	}
}
