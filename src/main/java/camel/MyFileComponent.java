package camel;

import java.io.File;
import java.util.Map;

import org.apache.camel.component.file.FileComponent;
import org.apache.camel.component.file.FileEndpoint;
import org.apache.camel.component.file.GenericFileConfiguration;
import org.apache.camel.component.file.GenericFileEndpoint;
import org.apache.camel.util.FileUtil;
import org.apache.camel.util.StringHelper;

public class MyFileComponent extends FileComponent {

	@Override
	protected GenericFileEndpoint<File> buildFileEndpoint(String uri, String remaining, Map<String, Object> parameters)
		throws Exception {
		// the starting directory must be a static (not containing dynamic expressions)
        if (StringHelper.hasStartToken(remaining, "simple")) {
            throw new IllegalArgumentException("Invalid directory: " + remaining
                    + ". Dynamic expressions with ${ } placeholders is not allowed."
                    + " Use the fileName option to set the dynamic expression.");
        }

        File file = new File(remaining);

        MyFileEndpoint result = new MyFileEndpoint(uri, this);
        result.setFile(file);

        GenericFileConfiguration config = new GenericFileConfiguration();
        config.setDirectory(FileUtil.isAbsolute(file) ? file.getAbsolutePath() : file.getPath());
        result.setConfiguration(config);

        return result;
	}

}
