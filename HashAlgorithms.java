/**
 * This class includes all the Hashing algorithms as static subclasses which implements the interfaces.
 * @author Luke
 */
public class HashAlgorithms {
	protected long hash;
	
	/**
	 * Very simple hash.
	 * extends the outer-class and implements the interfaces
	 * @author Luke Elam
	 */ 
	static class AddMultiHash extends HashAlgorithms implements PublicInterfaces.HashGenInterface {
		
		public void produceFileHash(byte[] fileBytes, int fileSize, int multiple) {	
			for (byte b : fileBytes) {
					this.hash += this.hash +  b + multiple + fileSize;
					this.hash *=  this.hash *  b *  multiple * fileSize;
				}
		}
		
		public void produceDirHash(byte[] fileBytes, int fileSize, int multiple,  long lastModified) {
			for (byte b : fileBytes) {
				this.hash +=  this.hash + b + multiple + fileSize + lastModified;
				this.hash *=   this.hash * b * multiple * fileSize * lastModified ;
			}
		}

		public void produceDirMetaHash(int fileSize, int multiple, long lastModified) {
			for (int i = 0; i < fileSize; i++) {
				this.hash += this.hash + multiple + fileSize + lastModified;
				this.hash *=  this.hash * multiple * fileSize * lastModified;
			}
		}
		
		@Override
		public long getHash() {
			return this.hash;
		}
	}

	/**
	 * This hash class is a little more complex. 
	 * Everything that is passed through it mixed up several times, bits shifted, XOR'd by themselves etc.
	 * This mix methods are unique to this Hash implementation.
	 * extends the outer-class and implements the interfaces
	 * @author  Luke Elam
	 */
	static class ShiftXORHash extends HashAlgorithms  implements PublicInterfaces.HashGenInterface, PublicInterfaces.HashGenExtendedInterface {
		
		/**
		 * responsible for mixing all three arguments.
		 * Shifts bits, XOR's them, etc etc.
		 * @param a
		 * @param c 
		 * @param b
		 * @return
		 */
		public long mix(byte a, int c, int b) {
			a -= b; a -= c; a ^= (c >> 13);
			b -= c; b -= a; b ^= (a << 8);
			c -= a; c -= b; c ^= (b >> 13);
			a -= b; a -= c; a ^= (c >> 12);
			b -= c; b -= a; b ^= (a << 16);
			c -= a; c -= b; c ^= (b >> 5); 
			a -= b; a -= c; a ^= (c >> 3);
			b -= c; b -= a; b ^= (a << 10);
			c -= a; c -= b; c ^= (b >> 15);
			return a^b^c;
		}
		
		public long mix(byte a, long c, int b) {
			a -= b; a -= c; a ^= (c >> 13);
			b -= c; b -= a; b ^= (a << 8);
			c -= a; c -= b; c ^= (b >> 13);
			a -= b; a -= c; a ^= (c >> 12);
			b -= c; b -= a; b ^= (a << 16);
			c -= a; c -= b; c ^= (b >> 5); 
			a -= b; a -= c; a ^= (c >> 3);
			b -= c; b -= a; b ^= (a << 10);
			c -= a; c -= b; c ^= (b >> 15);
			return a^b^c;
		}
		
		public long mix(int a, int c, long b) {
			a -= b; a -= c; a ^= (c >> 13);
			b -= c; b -= a; b ^= (a << 8);
			c -= a; c -= b; c ^= (b >> 13);
			a -= b; a -= c; a ^= (c >> 12);
			b -= c; b -= a; b ^= (a << 16);
			c -= a; c -= b; c ^= (b >> 5); 
			a -= b; a -= c; a ^= (c >> 3);
			b -= c; b -= a; b ^= (a << 10);
			c -= a; c -= b; c ^= (b >> 15);
			return a^b^c;
		}

		public void produceFileHash(byte[] fileBytes, int fileSize, int multiple) {
			for(byte b : fileBytes) {
				this.hash ^= mix(b, fileSize, multiple) * mix(b, fileSize, multiple);
			}
		}
		
		public void produceDirHash(byte[] fileBytes, int fileSize, int multiple, long lastModified) {
			for(byte b : fileBytes) {
				long secondVariable = fileSize ^ b * lastModified;
				this.hash ^= mix(b, secondVariable, multiple) * mix(b, secondVariable, multiple);
			}
		}

		public void produceDirMetaHash(int fileSize, int multiple, long lastModified) {
			this.hash ^= mix(fileSize, multiple, lastModified) * mix(multiple, fileSize, lastModified);
		}
		
		@Override
		public long getHash() {
			return this.hash;
		}

	}

	/**x
	 * This class is slightly more complex than the simple hash.
	 * You can see below how it works. Involves XOR'ing and bitwise operators in either direction.
	 * extends the outer-class and implements the interfaces
	 */
	static class OATHash extends HashAlgorithms  implements PublicInterfaces.HashGenInterface {
		
		public void produceFileHash(byte[] fileBytes, int fileSize, int multiple) {
			for(byte b : fileBytes) {
				this.hash ^= b;
				this.hash ^= fileSize;
				this.hash ^= multiple;
				this.hash ^= (this.hash << 10);
				this.hash ^= (this.hash << fileSize << 10);
				this.hash ^= (this.hash <<fileSize << multiple << 10);
				this.hash ^= (this.hash >> 6);
				this.hash ^= (this.hash >> fileSize >> 6);
				this.hash ^= (this.hash >> multiple >> fileSize >> 6);
			}
		}
		
		public void produceDirHash(byte[] fileBytes, int fileSize, int multiple, long lastModified) {
			for(byte b : fileBytes) {
				this.hash ^= b;
				this.hash ^= fileSize;
				this.hash ^= multiple;
				this.hash ^= (this.hash << 10);
				this.hash ^= (this.hash << fileSize << 10);
				this.hash ^= (this.hash << fileSize << multiple << 10);
				this.hash ^= (this.hash << fileSize << multiple << lastModified << 10);
				this.hash ^= (this.hash >> 6);
				this.hash ^= (this.hash >> fileSize >> 6);
				this.hash ^= (this.hash >> multiple >> fileSize >> 6);
				this.hash ^= (this.hash >> lastModified >> multiple >> fileSize >> 6);
			}
		}

		public void produceDirMetaHash(int fileSize, int multiple, long lastModified) {
			this.hash ^= fileSize;
			this.hash ^= multiple;
			this.hash ^= lastModified;
			this.hash ^= (this.hash << 10);
			this.hash ^= (this.hash << fileSize << 10);
			this.hash ^= (this.hash << fileSize << multiple << 10);
			this.hash ^= (this.hash << fileSize << multiple << lastModified << 10);
			this.hash ^= (this.hash >> 6);
			this.hash ^= (this.hash >> fileSize >> 6);
			this.hash ^= (this.hash >> fileSize  >> multiple >> 6);
			this.hash ^= (this.hash >> fileSize >> multiple >> lastModified >> 6);
		}
		
		@Override
		public long getHash() {
			return this.hash;
		}
		
	}

}