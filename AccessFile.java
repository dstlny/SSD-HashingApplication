import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Class which extends JPanel. This is the Hashing panel, explained below.
 */
public class AccessFile implements PublicInterfaces.FileAccessInterface {
	
	/**
	 * Start of variables used.
	 */
	private byte[] fileContents;
	private File importedFile;
	private File[] directoryFileList;
	private ArrayList<String> directoryFilenameList = new ArrayList<String>();
	private ArrayList<Date> directoryLastModifiedList = new ArrayList<Date>();
	private PublicInterfaces.HashGenInterface hashGen;
	private PublicInterfaces.DialogInterface interForDialogs =  new Dialogs();
	private String importedFileName, importedFilePath,importedFileParentPath;
	private long importedFileLastModified;
	private int importedFileSize;
	private int largestPrimeInINTLiteral = 214748357; //Largest prime number which will fit into aINT literal.
	/**
	 * End of variables used.
	 */

	public AccessFile(String inDirectory, String inName, int inChoice1, int inChoice2)  throws IOException {		
		if(inChoice1 == 1) singleFile(inName, inDirectory, inChoice2);
		if(inChoice1 == 2) multipleFiles(inName, inDirectory, inChoice2);
		if(inChoice1 == 3) multipleFilesMetaData(inName, inDirectory, inChoice2);
	}
	
	public void singleFile(String inName, String inDirectory, int inChoice2) throws IOException {
		importedFile = new File(inDirectory);
		importedFileSize = (int) importedFile.length();
		importedFilePath = importedFile.getParent();
		
		try {
			fileContents = readFileContent(inDirectory);
		} catch (FileSystemException e) {
			interForDialogs.fileSystemError();
		} catch (IOException e) {
			interForDialogs.exceptionIO();
		} finally {
			
			algorithmSelect(inChoice2);
			this.hashGen.produceFileHash(fileContents, importedFileSize, largestPrimeInINTLiteral);
			Output.setFileOutput(inName, this.hashGen.getHash());
			
			if(FilePanel.getWriteToDisk() == true) {
				new OpenAndWriteFile("Final.txt", importedFilePath, inName, inChoice2, this.hashGen.getHash(), FilePanel.getSelectedOption());	
			}
		}

	}
	public void multipleFiles(String inName, String inDirectory, int inChoice2) throws IOException {
		
		 importedFile = new File(inDirectory);
		 Path path = Paths.get(inDirectory);
		 directoryFileList = importedFile.listFiles();

		   if (directoryFileList != null) {
			   for(File next : directoryFileList){
				   boolean isFile = next.isFile();
					if (isFile) {

						int i;
						importedFilePath = next.getAbsolutePath();
						importedFileSize = (int) next.length();
						importedFileName = next.getName();
						importedFileParentPath = next.getParent();
						importedFileLastModified = next.lastModified();
						directoryFilenameList.add(importedFileName);
						
							try {
								fileContents = readFileContent(importedFilePath);
							} catch(FileSystemException e) {
								interForDialogs.fileSystemError();
							} catch (IOException e) {
								interForDialogs.exceptionIO();
							} finally {
								algorithmSelect(inChoice2);
								this.hashGen.produceDirHash(fileContents, importedFileSize, largestPrimeInINTLiteral, importedFileLastModified);
							   	Output.setFileOutput(importedFileParentPath, directoryFilenameList, this.hashGen.getHash());
							   	
							   	if(FilePanel.getWriteToDisk() == true) {
							   		new OpenAndWriteFile("Final.txt", importedFileParentPath, importedFileName, inChoice2, this.hashGen.getHash(), FilePanel.getSelectedOption());	
								}
							}
					}
			   }
		   }
	}
	public void multipleFilesMetaData(String inName, String inDirectory, int inChoice2) throws IOException {
		
		importedFile = new File(inDirectory);
		directoryFileList = importedFile.listFiles();
		
		if (directoryFileList != null) {
		   for(File next : directoryFileList){
			   boolean isFile = next.isFile();
				if (isFile) {	
					importedFilePath = next.getAbsolutePath();
					importedFileLastModified = next.lastModified();
  					importedFileName = next.getName();
  					importedFileSize = (int) next.length();
  					importedFileParentPath = next.getParent();
					directoryLastModifiedList.add(new Date(importedFileLastModified));		
					directoryFilenameList.add(importedFileName);	

					algorithmSelect(inChoice2);
					this.hashGen.produceDirMetaHash(importedFileSize, largestPrimeInINTLiteral, importedFileLastModified);
					Output.setFileOutputBasedOnMetaData(importedFileParentPath, directoryFilenameList, directoryLastModifiedList, this.hashGen.getHash());
					
					if(FilePanel.getWriteToDisk() == true) {
						new OpenAndWriteFile("Final.txt", importedFileParentPath, importedFileName, inChoice2, this.hashGen.getHash(), FilePanel.getSelectedOption());	
					}
				}
		   }
		}
	}

	/**
	 * Decides which algorithm to instantiate based on the choice of the user.
	 * @param algorithmChoice
	 */
	public void algorithmSelect(int algorithmChoice){
		this.hashGen = (algorithmChoice == 1) ? new HashAlgorithms.AddMultiHash() : (algorithmChoice == 2) ? new HashAlgorithms.ShiftXORHash() : (algorithmChoice == 3) ?  new HashAlgorithms.OATHash() :  new HashAlgorithms.AddMultiHash();
	}
	
	public byte[] readFileContent(String pathname) throws IOException, FileSystemException {
		return Files.readAllBytes(Paths.get(pathname));
	}
}

/**
 * Simple class which compares the contents of the stored file "Final.txt",
 * to the data the user has chosen. 
 * This is done linearly
 * @author Luke
 */
class OpenAndWriteFile  implements PublicInterfaces.WriteToDisk {
	
	private ArrayList<String> tempImportList = new ArrayList<String>();
	private ArrayList<String> Directories = new ArrayList<String>();
	private ArrayList<String> Filenames = new ArrayList<String>();
	private ArrayList<Integer> HashFunctionUsed = new ArrayList<Integer>();
	private ArrayList<Integer> inspectionSelection = new ArrayList<Integer>();
	private ArrayList<Long> Hashs = new ArrayList<Long>();
	private Set<String> tamperedList = new HashSet<String>();
	private Set<String> tamperedName = new HashSet<String>();
	private String nextFileLine;
	private Set<String> lineToRemove = new HashSet<String>(); 
	private Set<String> lineToKeep = new HashSet<String>(); 
	private String Hash;
	private File file = new File("");
	private String fullFormattedStored;
	private PublicInterfaces.DialogInterface interForDialogs =  new Dialogs();

	public OpenAndWriteFile(String fileToScan, String directory, String fileName, int hashFunction, long Hash, int inspectionSelect) throws IOException {
		String fullFormatted = directory + " : " + fileName + " : " + hashFunction + " : " + inspectionSelect  + " : " + Hash;
		String formatted = directory + " : " + fileName + " : " + hashFunction + " : " + inspectionSelect;
		this.Hash = String.format("%016X", Hash);
		
		try {
			/**
			 * Iterate over all lines of the file, add them to a list.
			 */
			BufferedReader in = new BufferedReader(new FileReader(fileToScan));
			while ((nextFileLine = in.readLine()) != null){
					tempImportList.add(nextFileLine);
			}
			in.close();
			
			if(tempImportList.isEmpty()) {
				file = new File("temp.txt");
				File finalName = new File("Final.txt");
				BufferedWriter writeFinal =  new BufferedWriter(new  FileWriter(file, true));	
				
				if(finalName.exists()) {
					boolean delete = finalName.delete();
					
					if(delete) {
						writeFinal.write(fullFormatted);
						writeFinal.newLine();
						writeFinal.close();
						boolean success = file.renameTo(finalName);
						
						if(success) {
							lineToKeep.add(fullFormatted);
							WriteFile("Final.txt");
						} else {
							System.out.println("Unsucessfuly renamed " + file.getName() + " to " +finalName.getName());
						}
					} else {
						System.out.println("Unsucessfuly deleted " +finalName.getName());
					}
					
				}
			} else {
				/**
				 * Split that list into a conventional array on every colon.
				 */
				for(int i = 0; i < tempImportList.size(); i++ ) {
					String[] importedData = tempImportList.get(i).split(" : ");
					Directories.add(importedData[0].trim());
					Filenames.add(importedData[1].trim());
					HashFunctionUsed.add(Integer.parseInt(importedData[2].trim()));
					inspectionSelection.add(Integer.parseInt(importedData[3].trim()));
					Hashs.add(Long.parseLong(importedData[4].trim()));
				}
				
				/**
				 * set1 includes the entire strings from the imported data.
				 */
				ArrayList<String> set1 = new ArrayList<String>();
				for(int i = 0; i < Directories.size(); i++) {
					set1.add(Directories.get(i) + " : " + Filenames.get(i) + " : " + HashFunctionUsed.get(i) + " : " + inspectionSelection.get(i) + " : " + Hashs.get(i)); 
					Collections.sort(set1);
				}
				
				/**
				 * set2 includes the entire strings from the imported data except the Hash.
				 */
				ArrayList<String> set2 = new ArrayList<String>();
				for(int i = 0; i < Directories.size(); i++) {
					set2.add(Directories.get(i) + " : " + Filenames.get(i) + " : " + HashFunctionUsed.get(i) + " : " + inspectionSelection.get(i)); 
					Collections.sort(set2);
				}
				
				/**
				 * set3 is a list of all the hashes that have been parsed from the file.
				 */
				ArrayList<Long> set3 = new ArrayList<Long>();
				for(int i = 0; i < Hashs.size(); i++) {
					set3.add(Hashs.get(i)); 
					Collections.sort(set3);
				}

				/**
				 * Is a rather crude way of removing duplicates and 
				 * comparing input to stored, but actually works - so happy.
				 **/
				for(int i = 0; i < Directories.size(); i++) {
					fullFormattedStored = Directories.get(i) + " : " + Filenames.get(i) + " : " + HashFunctionUsed.get(i) + " : " + inspectionSelection.get(i) + " : " + Hashs.get(i); 
					
					if(set1.contains(fullFormatted)) {
						lineToKeep.add(fullFormattedStored);
					    lineToRemove.add("(duplicate) : " + fullFormatted);
					    continue;
					} else if(set2.contains(formatted)) {
								for(Long s : set3) {
									if(set3.contains(Hash)) {
										lineToKeep.add(fullFormatted);
									    lineToRemove.add("old stored (duplicate): " + fullFormattedStored);
									    continue;
									}  else if(set3.contains(Hash) == false && s != Hash) {
										for(int j = 0; j < Directories.size(); j++) {
												if(String.valueOf(Directories.get(i) + " : " + Filenames.get(i) + " : " + HashFunctionUsed.get(i) + " : " + inspectionSelection.get(i)).equals(String.valueOf(directory + " : " + fileName + " : " + hashFunction + " : " + inspectionSelect))) {
													lineToRemove.add("(tampered) : " + fullFormattedStored);
													lineToKeep.add(fullFormatted);
													tamperedName.add(fileName);
													tamperedList.add("Old hash: " +String.format("%016X", Hashs.get(i)) + "\nNew hash: " + this.Hash);

													/**
													 * Sometimes this somehow sneaks through even though i'm adding it to the lines to remove.
													 * ¯\_(ツ)_/¯ 
													 * */
													if(lineToKeep.contains(fullFormattedStored)) {
														lineToKeep.remove(fullFormattedStored);
													}
												    continue;
												    
												    /**
													 * This should catch duplicates/files tampered even if the directory is different from the ones stored in the file;
													 */
												} else if(String.valueOf(Filenames.get(i) + " : " + HashFunctionUsed.get(i) + " : " + inspectionSelection.get(i)).equals(String.valueOf(fileName + " : " + hashFunction + " : " + inspectionSelect))) {
													lineToRemove.add("(tampered) : " + fullFormattedStored);
													lineToKeep.add(fullFormatted);
													tamperedName.add(fileName);
													tamperedList.add("Old hash: " +String.format("%016X", Hashs.get(i)) + "\nNew hash: " + this.Hash);

													if(lineToKeep.contains(fullFormattedStored)) {
														lineToKeep.remove(fullFormattedStored);
													}
													
													continue;

												} else {
													lineToKeep.add(fullFormattedStored);
													continue;
												}
										}
									}
								}
					} else {
						lineToKeep.add(fullFormatted);
					    lineToKeep.add(fullFormattedStored);
					    continue;
					}
				}
			}
			/**
			 * If the file is not found then we can assume the file doesn't exist.
			 * If that is the case, write to the file the current passed through file information.
			 */
		} catch (FileNotFoundException e) {
			file = new File("temp.txt");
			File newFileName = new File("Final.txt");
			BufferedWriter writeFinal =  new BufferedWriter(new  FileWriter(file, true));	
			writeFinal.append(fullFormatted);
			writeFinal.newLine();
			writeFinal.close();
			boolean success = file.renameTo(newFileName);

			if(success) {
				System.out.println("Sucessfuly renamed " + file.getName() + " to " +newFileName.getName());
			} else {
				System.out.println("Unsucessfuly renamed " + file.getName() + " to " +newFileName.getName());
			}
		} finally {
			WriteFile("Final.txt");
		}
	}
	
	public void WriteFile(String fileToScan)  throws IOException, FileNotFoundException {
		  
	 try {
		    BufferedWriter writeFinal =  new BufferedWriter(new  FileWriter(fileToScan));	
		    //loop through our buffer of lines to Keep (non-duplicates, unique strings)
		    for (String b : lineToKeep) {
		    	writeFinal.append(b);
		    	writeFinal.newLine();
			}
			 //close the writer
			 writeFinal.close();
	 } catch (FileNotFoundException e) {
		 	  interForDialogs.fileNotFound(fileToScan);
	 } catch (IOException e) {
		      interForDialogs.exceptionIO();
	 } finally {
		   if(tamperedList.size() > 0) {
			   interForDialogs.FilePossiblyTampered(tamperedList, tamperedName);
			}
	   }
   }
}