package com.if2210.app.datastore;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.if2210.app.factory.AnimalCardFactory;
import com.if2210.app.factory.ItemCardFactory;
import com.if2210.app.factory.ProductCardFactory;
import com.if2210.app.model.AnimalCardModel;
import com.if2210.app.model.CardModel;
import com.if2210.app.model.DeckModel;
import com.if2210.app.model.PlantCardModel;
import com.if2210.app.model.PlayerModel;
import com.if2210.app.model.ProductCardModel;

public class DataManager {
    private Path path;
    private Path gameStatePath;
    private Path player1Path;
    private Path player2Path;

    private int currentTurn;
    private Map<ProductCardModel, Integer> productList;
    private PlayerModel player1;
    private PlayerModel player2;

    public DataManager(Path path) {
        this.path = path;
        this.gameStatePath = this.path.resolve("gamestate.txt");
        this.player1Path = this.path.resolve("player1.txt");
        this.player2Path = this.path.resolve("player2.txt");

        this.currentTurn = 0;
        this.productList = new HashMap<ProductCardModel, Integer>();
    }

    public Path getPath() {
        return path;
    }

    public Path getGameStatePath() {
        return gameStatePath;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(int currentTurn) {
        this.currentTurn = currentTurn;
    }

    public PlayerModel getPlayer1() {
        return player1;
    }

    public void setPlayer1(PlayerModel player1) {
        this.player1 = player1;
    }

    public PlayerModel getPlayer2() {
        return player2;
    }

    public void setPlayer2(PlayerModel player2) {
        this.player2 = player2;
    }

    public Map<ProductCardModel, Integer> getProductList() {
        return productList;
    }

    public void setProductList(Map<ProductCardModel, Integer> productList) {
        this.productList = productList;
    }

    private String isAnimalCard(String name) {
        switch (name) {
            case "HIU_DARAT":
                return "Hiu Darat";
            case "SAPI":
                return "Sapi";
            case "DOMBA":
                return "Domba";
            case "KUDA":
                return "Kuda";
            case "AYAM":
                return "Ayam";
            case "BERUANG":
                return "Beruang";
            default:
                return null;
        }
    }

    private String isPlantCard(String name) {
        switch (name) {
            case "BIJI_LABU":
                return "Biji Labu";
            case "BIJI_JAGUNG":
                return "Biji Jagung";
            case "BIJI_STROBERI":
                return "Biji Stroberi";
            default:
                return null;
        }
    }

    private String isProductCard(String name) {
        switch (name) {
            case "LABU":
                return "Labu";
            case "JAGUNG":
                return "Jagung";
            case "STROBERI":
                return "Stroberi";
            case "SUSU":
                return "Susu";
            case "TELUR":
                return "Telur";
            case "SIRIP_HIU":
                return "Sirip Hiu";
            case "DAGING_KUDA":
                return "Daging Kuda";
            case "DAGING_DOMBA":
                return "Daging Domba";
            case "DAGING_BERUANG":
                return "Daging Beruang";
            default:
                return null;
        }
    }

    private String isItemCard(String name) {
        switch (name) {
            case "ACCELERATE":
                return "Accelerate";
            case "DELAY":
                return "Delay";
            case "INSTANT_HARVEST":
                return "Instant Harvest";
            case "DESTROY":
                return "Destroy";
            case "PROTECT":
                return "Protect";
            case "TRAP":
                return "Trap";
            default:
                return null;
        }
    }

    private int codeToActiveIndex(String code) {
        switch (code) {
            case "A01":
                return 0;
            case "B01":
                return 1;
            case "C01":
                return 2;
            case "D01":
                return 3;
            case "E01":
                return 4;
            case "F01":
                return 5;
            default:
                return -1;
        }
    }

    private int[] codeToFieldIndex(String code) {
        char column = code.charAt(0);
        int row = Integer.parseInt(code.substring(1));

        int columnIndex;
        int rowIndex = row - 1; // Adjust row number to start from 0

        switch (column) {
            case 'A':
                columnIndex = 0;
                break;
            case 'B':
                columnIndex = 1;
                break;
            case 'C':
                columnIndex = 2;
                break;
            case 'D':
                columnIndex = 3;
                break;
            case 'E':
                columnIndex = 4;
                break;
            default:
                throw new IllegalArgumentException("Invalid column: " + column);
        }

        if (rowIndex < 0 || rowIndex > 3) {
            throw new IllegalArgumentException("Invalid row: " + row);
        }

        return new int[] { rowIndex, columnIndex };
    }

    public void save(String folderName, int currentTurn, Map<ProductCardModel, Integer> productList,
            PlayerModel player1, PlayerModel player2) {
        try {
            this.path = Paths.get(getClass().getResource("/com/if2210/app/gamestates/" + folderName).toURI());
            this.gameStatePath = this.path.resolve("gamestate.txt");
            this.player1Path = this.path.resolve("player1.txt");
            this.player2Path = this.path.resolve("player2.txt");

            // Check if the folder exists, if not, create it
            if (!Files.exists(this.path)) {
                Files.createDirectories(this.path);
            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
            return;
        }

        saveGameState(currentTurn, productList);
        savePlayerData(player1, player1Path);
        savePlayerData(player2, player2Path);
    }

    private void saveGameState(int currentTurn, Map<ProductCardModel, Integer> productList) {
        try {
            List<String> lines = new ArrayList<>();
            lines.add(Integer.toString(currentTurn));
            lines.add(Integer.toString(productList.size()));
            for (Map.Entry<ProductCardModel, Integer> entry : productList.entrySet()) {
                lines.add(entry.getKey().getName() + " " + entry.getValue());
            }
            Files.write(gameStatePath, lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void savePlayerData(PlayerModel player, Path playerPath) {
        try {
            List<String> lines = new ArrayList<>();
            lines.add(Integer.toString(player.getMoney()));
            lines.add(Integer.toString(player.getDeck().getDeckSize()));
            lines.add(Integer.toString(player.getActiveDeck().getEffectiveDeckSize()));
            for (int i = 0; i < 6; i++) {
                if (player.getActiveDeck().getCard(i) != null) {
                    lines.add("A" + String.format("%02d", i) + " " + player.getActiveDeck().getCard(i).getName());
                }
            }
            lines.add(Integer.toString(player.getField().getEffectiveFieldSize()));
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 5; j++) {
                    CardModel card = player.getField().getCard(i, j);
                    if (card != null) {
                        if (card instanceof AnimalCardModel) {
                            AnimalCardModel animalCard = (AnimalCardModel) card;
                            lines.add(String.format("%c%02d %s %d %d", 'A' + i, j + 1, animalCard.getName(),
                                    animalCard.getCurrentWeight(), animalCard.getActiveItems().size()));
                            for (int k = 0; k < animalCard.getActiveItems().size(); k++) {
                                lines.add(animalCard.getActiveItems().get(k).getName());
                            }
                        } else if (card instanceof PlantCardModel) {
                            PlantCardModel plantCard = (PlantCardModel) card;
                            lines.add(String.format("%c%02d %s %d %d", 'A' + i, j + 1, plantCard.getName(),
                                    plantCard.getCurrentAge(), plantCard.getActiveItems().size()));
                            for (int k = 0; k < plantCard.getActiveItems().size(); k++) {
                                lines.add(plantCard.getActiveItems().get(k).getName());
                            }
                        }
                    }
                }
            }
            Files.write(playerPath, lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load(String folderName) {
        try {
            URL url = getClass().getResource("/com/if2210/app/gamestates/" + folderName);
            if (url == null) {
                throw new IllegalArgumentException("Folder doesn't exist");
            }
            this.path = Paths.get(url.toURI());
            this.gameStatePath = this.path.resolve("gamestate.txt");
            this.player1Path = this.path.resolve("player1.txt");
            this.player2Path = this.path.resolve("player2.txt");
        } catch (URISyntaxException e) {
            System.out.println("Folder doesnt exist");
            return;
        }

        loadGameState();

        try {
            this.player1 = new PlayerModel();
            List<String> lines = new ArrayList<String>(Files.readAllLines(player1Path));
            int gulden = Integer.parseInt(lines.get(0));
            int deckCount = Integer.parseInt(lines.get(1));
            DeckModel deck = new DeckModel(deckCount);
            int activeDeckCount = Integer.parseInt(lines.get(2));

            for (int i = 3; i < activeDeckCount + 3; i++) {
                String[] parts = lines.get(i).split(" ");

                String name;

                name = isAnimalCard(parts[1]);
                if (name != null) {
                    AnimalCardModel card = AnimalCardFactory.createAnimalCard(name);
                    int index = codeToActiveIndex(parts[0]);
                    this.player1.getActiveDeck().setCard(index, card);
                    continue;
                }

                name = isPlantCard(parts[1]);
                if (name != null) {
                    AnimalCardModel card = AnimalCardFactory.createAnimalCard(name);
                    int index = codeToActiveIndex(parts[0]);
                    this.player1.getActiveDeck().setCard(index, card);
                    continue;
                }

                name = isProductCard(parts[1]);
                if (name != null) {
                    ProductCardModel card = ProductCardFactory.createProductCard(name);
                    this.player1.getDeck().getCards().add(card);
                    continue;
                }

                name = isItemCard(parts[1]);
                if (name != null) {
                    ProductCardModel card = ProductCardFactory.createProductCard(name);
                    this.player1.getDeck().getCards().add(card);
                    continue;
                }
            }

            int fieldCardCount = Integer.parseInt(lines.get(activeDeckCount + 3));

            for (int i = activeDeckCount + 4; i < activeDeckCount + 4 + fieldCardCount; i++) {
                String[] parts = lines.get(i).split(" ");

                String name;

                name = isAnimalCard(parts[1]);
                if (name != null) {
                    AnimalCardModel card = AnimalCardFactory.createAnimalCard(name);
                    card.setCurrentWeight(Integer.parseInt(parts[2]));
                    int activeItemCount = Integer.parseInt(parts[3]);
                    for (int j = 0; j < activeItemCount; j++) {
                        card.getActiveItems().add(ItemCardFactory.createItemCard(isItemCard(parts[4 +
                                j])));
                    }
                    int[] index = codeToFieldIndex(parts[0]);
                    this.player1.getField().setCard(card, index[0], index[1]);
                    continue;
                }

                name = isPlantCard(parts[1]);
                if (name != null) {
                    AnimalCardModel card = AnimalCardFactory.createAnimalCard(name);
                    card.setCurrentWeight(Integer.parseInt(parts[2]));
                    int activeItemCount = Integer.parseInt(parts[3]);
                    for (int j = 0; j < activeItemCount; j++) {
                        card.getActiveItems().add(ItemCardFactory.createItemCard(isItemCard(parts[4 +
                                j])));
                    }
                    int[] index = codeToFieldIndex(parts[0]);
                    this.player1.getField().setCard(card, index[0], index[1]);
                    continue;
                }
            }

            this.player1.setMoney(gulden);
            this.player1.setDeck(deck);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        try {
            this.player2 = new PlayerModel();
            List<String> lines = new ArrayList<String>(Files.readAllLines(player2Path));
            int gulden = Integer.parseInt(lines.get(0));
            int deckCount = Integer.parseInt(lines.get(1));
            DeckModel deck = new DeckModel(deckCount);
            int activeDeckCount = Integer.parseInt(lines.get(2));

            for (int i = 3; i < activeDeckCount + 3; i++) {
                String[] parts = lines.get(i).split(" ");

                String name;

                name = isAnimalCard(parts[1]);
                if (name != null) {
                    AnimalCardModel card = AnimalCardFactory.createAnimalCard(name);
                    int index = codeToActiveIndex(parts[0]);
                    this.player2.getActiveDeck().setCard(index, card);
                    continue;
                }

                name = isPlantCard(parts[1]);
                if (name != null) {
                    AnimalCardModel card = AnimalCardFactory.createAnimalCard(name);
                    int index = codeToActiveIndex(parts[0]);
                    this.player2.getActiveDeck().setCard(index, card);
                    continue;
                }

                name = isProductCard(parts[1]);
                if (name != null) {
                    ProductCardModel card = ProductCardFactory.createProductCard(name);
                    this.player2.getDeck().getCards().add(card);
                    continue;
                }

                name = isItemCard(parts[1]);
                if (name != null) {
                    ProductCardModel card = ProductCardFactory.createProductCard(name);
                    this.player2.getDeck().getCards().add(card);
                    continue;
                }
            }

            int fieldCardCount = Integer.parseInt(lines.get(activeDeckCount + 3));

            for (int i = activeDeckCount + 4; i < activeDeckCount + 4 + fieldCardCount; i++) {
                String[] parts = lines.get(i).split(" ");

                String name;

                name = isAnimalCard(parts[1]);
                if (name != null) {
                    AnimalCardModel card = AnimalCardFactory.createAnimalCard(name);
                    card.setCurrentWeight(Integer.parseInt(parts[2]));
                    int activeItemCount = Integer.parseInt(parts[3]);
                    for (int j = 0; j < activeItemCount; j++) {
                        card.getActiveItems().add(ItemCardFactory.createItemCard(isItemCard(parts[4 +
                                j])));
                    }
                    int[] index = codeToFieldIndex(parts[0]);
                    this.player2.getField().setCard(card, index[0], index[1]);
                    continue;
                }

                name = isPlantCard(parts[1]);
                if (name != null) {
                    AnimalCardModel card = AnimalCardFactory.createAnimalCard(name);
                    card.setCurrentWeight(Integer.parseInt(parts[2]));
                    int activeItemCount = Integer.parseInt(parts[3]);
                    for (int j = 0; j < activeItemCount; j++) {
                        card.getActiveItems().add(ItemCardFactory.createItemCard(isItemCard(parts[4 +
                                j])));
                    }
                    int[] index = codeToFieldIndex(parts[0]);
                    this.player2.getField().setCard(card, index[0], index[1]);
                    continue;
                }
            }

            this.player2.setMoney(gulden);
            this.player2.setDeck(deck);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    private void loadGameState() {
        try {
            List<String> lines = new ArrayList<String>(Files.readAllLines(gameStatePath));
            this.currentTurn = Integer.parseInt(lines.get(0));
            int itemCount = Integer.parseInt(lines.get(1));
            for (int i = 2; i < itemCount + 2; i++) {
                String[] parts = lines.get(i).split(" ");
                ProductCardModel product = ProductCardFactory.createProductCard(isProductCard(parts[0]));
                this.productList.put(product, Integer.parseInt(parts[1]));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }
}
