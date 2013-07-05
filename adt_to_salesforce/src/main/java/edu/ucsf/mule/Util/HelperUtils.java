package edu.ucsf.mule.Util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class HelperUtils {
	/**
	 * Deep Clone object passed using serialization
	 * 
	 * @param objToClone
	 * @return
	 */
	public static final Object deepClone(Object objToClone) {
		try {
			ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream(
					100);
			ObjectOutputStream objectoutputstream = new ObjectOutputStream(
					bytearrayoutputstream);
			objectoutputstream.writeObject(objToClone);
			byte abyte0[] = bytearrayoutputstream.toByteArray();
			objectoutputstream.close();
			ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(
					abyte0);
			ObjectInputStream objectinputstream = new ObjectInputStream(
					bytearrayinputstream);
			Object clone = objectinputstream.readObject();
			objectinputstream.close();
			return clone;
		} catch (Exception exception) {
			System.out.println("Error cloning " + objToClone + exception.getMessage());

		}
		return null;
	}

}
