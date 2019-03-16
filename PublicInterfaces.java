import java.io.*;
import java.nio.file.*;
import java.util.*;

public class PublicInterfaces {

	public interface DialogInterface  {
		public void aboutProgramDialog();
		public void fileSystemError();
		public void fileNotFound(String filename);
		public void exceptionIO();
		public void selectionCancelled();
		public void FilePossiblyTampered(Set<String> string, Set<String> name);
	}
	
	public interface HashGenInterface {
		public void produceFileHash(byte[] fileBytes, int fileSize, int multiple);
		public void produceDirHash(byte[] fileBytes, int fileSize, int multiple,  long lastModified);
		public void produceDirMetaHash(int fileSize, int multiple, long lastModified);
		public long getHash();
	}
	
	interface HashGenExtendedInterface extends HashGenInterface {
		public long mix(byte a, int c, int b);
		public long mix(byte a, long c, int b);
		public long mix(int a, int c, long b);
	}
	
	interface FileAccessInterface {
		public void singleFile(String inName, String inDirectory, int inChoice2) throws IOException;
		public void multipleFiles(String inName, String inDirectory, int inChoice2) throws IOException;
		public void multipleFilesMetaData(String inName, String inDirectory, int inChoice2) throws IOException;
		public void algorithmSelect(int algorithmChoice);
		public byte[] readFileContent(String pathname) throws IOException, FileSystemException ;
	}
	
	public interface WriteToDisk {
		public void WriteFile(String fileToScan)  throws IOException, FileNotFoundException;
	}

}