package hr.fer.zemris.java.hw07.crypto;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * A command-line program that can encrypt or decrypt a file using AES
 * algorithm. Also, it can check files digest using SHA-256 algorithm. To check
 * a file's digest first command-line argument must be 'checksha' and second
 * parameter is a path to the file. To encrypt a file, first command-line
 * argument must be 'encrypt', second command-line parameter is a file to
 * encrypt and third argument is a path to which encrypted file will be saved.
 * To decrypt a file, first command-line argument must be 'decrypt', second
 * command-line parameter is a file to decrypt and third argument is a path to
 * which decrypted file will be saved.
 * 
 * @author Disho
 *
 */
public class Crypto {
	private static final int BUFFER_SIZE = 4 * 1024;

	/**
	 * Method encrypts/decrypts given file using AES algorithm and saves the result
	 * to the given destination
	 * 
	 * @param file
	 *            a file to encrypt/decrypt
	 * @param destionation
	 *            resulted encrypted/decrypted file
	 * @param keyText
	 *            password as hex-encoded text (16 bytes, i.e. 32 hex-digits)
	 * @param ivText
	 *            initialization vector as hex-encoded text (32 hex-digits)
	 * @param encrypt
	 *            true if you want to encrypt, false if you want to decrypt
	 * @throws IOException
	 *             if something goes wrong with reading or writing to files
	 * @throws CipherException
	 *             if encryption/decryption goes wrong
	 */
	private static void encryptOrDecrypt(Path file, Path destionation, String keyText, String ivText, boolean encrypt)
			throws IOException, CipherException {
		SecretKeySpec keySpec = new SecretKeySpec(Util.hextobyte(keyText), "AES");
		AlgorithmParameterSpec paramSpec = new IvParameterSpec(Util.hextobyte(ivText));
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, keySpec, paramSpec);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
				| InvalidAlgorithmParameterException ignorable) {
		}

		try (InputStream is = getBufferedInputStream(file, BUFFER_SIZE);
				OutputStream os = getBufferedOutputStream(destionation, BUFFER_SIZE)) {
			byte[] buff = new byte[BUFFER_SIZE];
			while (true) {
				int r = is.read(buff);
				if (r < 0)
					break;
				os.write(cipher.update(buff, 0, r));
			}

			os.write(cipher.doFinal());

		} catch (IllegalBlockSizeException | BadPaddingException e) {
			throw new CipherException("Could not encrypt/decrypt a file.");
		}
	}

	/**
	 * Returns a files digest using SHA-256 algorithm.
	 * 
	 * @param path
	 *            file to digest
	 * @return files digest
	 * @throws IOException
	 *             if file cannot be opened
	 * @throws IllegalArgumentException
	 *             if path is a directory (not a regular file)
	 */
	private static String getDigest(Path path) throws IOException {
		if (!Files.isRegularFile(path)) {
			throw new IllegalArgumentException("Provided path is not a file. It must be a file.");
		}

		MessageDigest sha = null;

		try {
			sha = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException igonrable) {
		}

		try (InputStream is = getBufferedInputStream(path, BUFFER_SIZE)) {
			byte[] buff = new byte[BUFFER_SIZE];
			while (true) {
				int r = is.read(buff);
				if (r < 0)
					break;
				sha.update(buff, 0, r);
			}
		}

		return Util.bytetohex(sha.digest());
	}

	/**
	 * Returns a buffered input stream of a given file with the given buffer size.
	 * 
	 * @param file
	 *            file of which to create a stream
	 * @param size
	 *            buffer size (in bytes), must be a positive integer
	 * @return buffered input stream
	 * @throws IOException
	 *             if file cannot be opened
	 * @throws IllegalArgumentException
	 *             if buffer size <= 0
	 */
	private static InputStream getBufferedInputStream(Path file, int size) throws IOException {
		if (size <= 0)
			throw new IllegalArgumentException("Buffer size cannot be negative.");

		return new BufferedInputStream(Files.newInputStream(file, StandardOpenOption.READ), size);
	}

	/**
	 * Returns a buffered output stream of a given file with the given buffer size.
	 * 
	 * @param file
	 *            file for which to create a stream
	 * @param size
	 *            buffer size (in bytes), must be a positive integer
	 * @return buffered input stream
	 * @throws IOException
	 *             if file cannot be created
	 * @throws IllegalArgumentException
	 *             if buffer size <= 0
	 */
	private static OutputStream getBufferedOutputStream(Path file, int size) throws IOException {
		if (size <= 0)
			throw new IllegalArgumentException("Buffer size cannot be negative.");

		return new BufferedOutputStream(
				Files.newOutputStream(file, StandardOpenOption.CREATE, StandardOpenOption.WRITE), size);
	}

	/**
	 * Main method. Controls the flow of the program.
	 * 
	 * @param args
	 *            command-line arguments
	 * @throws CipherException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws IOException
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Arguments expected. Got none.");
			System.exit(0);
		}

		boolean encrypt = false;

		switch (args[0]) {
		case "checksha":
			System.out.print("Please provide expected sha-256 digest for " + args[1] + ":\n> ");

			String expected;
			try (Scanner sc = new Scanner(System.in)) {
				expected = sc.next();
			}

			String actual = null;
			try {
				actual = getDigest(Paths.get(args[1]));
			} catch (IOException e) {
				System.out.println("Couldnt open file " + args[1]);
				System.exit(0);
			}

			if (expected.equals(actual)) {
				System.out.println("Digesting completed. Digest of " + args[1] + " matches expected digest.");
			} else {
				System.out.println("Digesting completed. Digest of " + args[1] + " does not match expected digest. "
						+ "Expected digest was: " + actual);
			}
			break;

		case "encrypt":
			encrypt = true;
		case "decrypt":
			String keyText, ivText;

			try (Scanner sc = new Scanner(System.in)) {
				System.out.print("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits):\n> ");
				keyText = sc.next();
				System.out.print("Please provide initialization vector as hex-encoded text (32 hex-digits):\n> ");
				ivText = sc.next();
			}

			try {
				encryptOrDecrypt(Paths.get(args[1]), Paths.get(args[2]), keyText, ivText, encrypt);
			} catch (IOException e) {
				System.out.println("Could not open or create a file.");
				System.exit(0);
			} catch (CipherException e) {
				System.out.println(e.getMessage());
				System.exit(0);
			}

			if (encrypt) {
				System.out
						.println("Encryption completed. Generated file " + args[2] + " based on file " + args[1] + ".");
			} else {
				System.out
						.println("Decryption completed. Generated file " + args[2] + " based on file " + args[1] + ".");
			}

			break;

		default:
			System.out.println("Illegal command. It can only be: checksha, decypt or encrypt.");
		}
	}
}
