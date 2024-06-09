
public class DataHash {
    
    public static String passwordHash(String password , Double hashValue) {
        // Karma değerini hesaplama
	String hashedPassword = hashValue +  password + hashValue;
        return hashedPassword;
    }

}
