// Player class

public class Player {
   public int playerID;
   public String codename;
   public int hardwareID;
   public int score;
   public boolean atBase = false;

   public void hitBase() {
      atBase = true;
   }

   // Constructor
   public Player(int PlayerID, String Codename, int HardwareID) {
      playerID = PlayerID;
      codename = Codename;
      hardwareID = HardwareID;
      score = 0;
   }

   // Alternate instructor. Automatically converts Strings to Integers
   public Player(String PlayerID, String Codename, String HardwareID) {
      playerID = Integer.parseInt(PlayerID);
      codename = Codename;
      hardwareID = Integer.parseInt(HardwareID);
      score = 0;
   }

   // Copy constructor
   public Player(Player p) {
      playerID = p.playerID;
      codename = p.codename;
      hardwareID = p.hardwareID;
      score = p.score;
   }
}
