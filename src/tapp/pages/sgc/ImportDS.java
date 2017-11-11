package tapp.pages.sgc;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
//import org.apache.tapestry5.upload.services.UploadedFile;

/**
 * Reference: http://tapestry.formos.com/nightly/tapestry5/tapestry-upload/
 */
public class ImportDS {

//	@Property
//	private UploadedFile file;

	public void onSuccess() {
//		File copied = new File("/my/file/location/" + file.getFileName());

//		file.write(copied);
	}

	@Persist(PersistenceConstants.FLASH)
	@Property
	private String message;

	Object onUploadException(FileUploadException ex) {
		message = "Upload exception: " + ex.getMessage();

		return this;
	}
}
