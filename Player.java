// Player class

public class Player {
   public int playerID;
   public String codename;
   public int hardwareID;

   // Constructor
   public Player(int PlayerID, String Codename, int HardwareID) {
      playerID = PlayerID;
      codename = Codename;
      hardwareID = HardwareID;
   }

   // Alternate instructor. Automatically converts Strings to Integers
   public Player(String PlayerID, String Codename, String HardwareID) {
      playerID = Integer.parseInt(PlayerID);
      codename = Codename;
      hardwareID = Integer.parseInt(HardwareID);
   }
}
