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

    private Path resourcePath;
    private Path gameStateResourcePath;
    private Path player1ResourcePath;
    private Path player2ResourcePath;

    private int currentTurn;
    private Map<ProductCardModel, Integer> productList;
    private PlayerModel player1;
    private PlayerModel player2;

    public DataManager() {
        try {
            this.path = Paths.get(getClass().getResource("/com/if2210/app/gamestates/state0").toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        this.gameStatePath = this.path.resolve("gamestate.txt");
        this.player1Path = this.path.resolve("player1.txt");
        this.player2Path = this.path.resolve("player2.txt");

        this.currentTurn = 0;
        this.productList = new HashMap<ProductCardModel, Integer>();
    }

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

    private String activeIndexToCode(int index) {
        switch (index) {
            case 0:
                return "A01";
            case 1:
                return "B01";
            case 2:
                return "C01";
            case 3:
                return "D01";
            case 4:
                return "E01";
            case 5:
                return "F01";
            default:
                return null;
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

    private String fieldIndexToCode(int[] index) {
        char column;
        int row = index[0] + 1; // Adjust row number to start from 1

        switch (index[1]) {
            case 0:
                column = 'A';
                break;
            case 1:
                column = 'B';
                break;
            case 2:
                column = 'C';
                break;
            case 3:
                column = 'D';
                break;
            case 4:
                column = 'E';
                break;
            default:
                throw new IllegalArgumentException("Invalid column: " + index[1]);
        }

        return column + String.format("%02d", row);
    }

    public void save(String folderName, int currentTurn, Map<ProductCardModel, Integer> productList,
            PlayerModel player1, PlayerModel player2) {
        try {
            URL url = getClass().getResource("/com/if2210/app/gamestates/" + folderName);
            // If folder doesn't exist, create it and its files
            if (url == null && Paths.get("src/main/resources/com/if2210/app/gamestates", folderName) == null) {
                Files.createDirectory(
                        Paths.get(getClass().getResource("/com/if2210/app/gamestates").toURI()).resolve(folderName));
                Files.createDirectory(
                        Paths.get("src/main/resources/com/if2210/app/gamestates", folderName));

                Files.createFile(Paths.get(getClass().getResource("/com/if2210/app/gamestates").toURI())
                        .resolve(folderName).resolve("gamestate.txt"));
                Files.createFile(
                        Paths.get("src/main/resources/com/if2210/app/gamestates", folderName).resolve("gamestate.txt"));

                Files.createFile(Paths.get(getClass().getResource("/com/if2210/app/gamestates").toURI())
                        .resolve(folderName).resolve("player1.txt"));
                Files.createFile(
                        Paths.get("src/main/resources/com/if2210/app/gamestates", folderName).resolve("player1.txt"));

                Files.createFile(Paths.get(getClass().getResource("/com/if2210/app/gamestates").toURI())
                        .resolve(folderName).resolve("player2.txt"));
                Files.createFile(
                        Paths.get("src/main/resources/com/if2210/app/gamestates", folderName).resolve("player2.txt"));
                url = getClass().getResource("/com/if2210/app/gamestates/" + folderName);
            }
            this.path = Paths.get(url.toURI());
            this.resourcePath = Paths.get("src/main/resources/com/if2210/app/gamestates", folderName);
            this.gameStatePath = this.path.resolve("gamestate.txt");
            this.gameStateResourcePath = this.resourcePath.resolve("gamestate.txt");
            this.player1Path = this.path.resolve("player1.txt");
            this.player1ResourcePath = this.resourcePath.resolve("player1.txt");
            this.player2Path = this.path.resolve("player2.txt");
            this.player2ResourcePath = this.resourcePath.resolve("player2.txt");
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
            return;
        }

        saveGameState(currentTurn, productList);
        savePlayerData(player1, player1Path, player1ResourcePath);
        savePlayerData(player2, player2Path, player2ResourcePath);
    }

    private void saveGameState(int currentTurn, Map<ProductCardModel, Integer> productList) {
        try {
            List<String> lines = new ArrayList<>();
            lines.add(Integer.toString(currentTurn));
            lines.add(Integer.toString(productList.size()));
            for (Map.Entry<ProductCardModel, Integer> entry : productList.entrySet()) {
                String name = "";
                String parts[] = entry.getKey().getName().split(" ");
                for (int i = 0; i < parts.length; i++) {
                    name += parts[i].toUpperCase();
                    if (i < parts.length - 1) {
                        name += "_";
                    }
                }
                lines.add(name + " " + entry.getValue());
            }
            Files.write(gameStatePath, lines);
            Files.write(gameStateResourcePath, lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void savePlayerData(PlayerModel player, Path playerPath, Path playerResourcePath) {
        try {
            List<String> lines = new ArrayList<>();
            lines.add(Integer.toString(player.getMoney()));
            lines.add(Integer.toString(player.getDeck().getDeckSize()));
            lines.add(Integer.toString(player.getActiveDeck().getEffectiveDeckSize()));
            for (int i = 0; i < 6; i++) {
                if (player.getActiveDeck().getCard(i) != null) {
                    String name = "";
                    String parts[] = player.getActiveDeck().getCard(i).getName().split(" ");
                    for (int j = 0; j < parts.length; j++) {
                        name += parts[j].toUpperCase();
                        if (j < parts.length - 1) {
                            name += "_";
                        }
                    }
                    lines.add(activeIndexToCode(i) + " " + name);
                }
            }
            lines.add(Integer.toString(player.getField().getEffectiveFieldSize()));
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 5; j++) {
                    CardModel card = player.getField().getCard(i, j);
                    if (card != null) {
                        if (card instanceof AnimalCardModel) {
                            AnimalCardModel animalCard = (AnimalCardModel) card;
                            String activeItem = "";
                            for (int k = 0; k < animalCard.getActiveItems().size(); k++) {
                                String itemName = "";
                                String parts[] = animalCard.getActiveItems().get(k).getName().split(" ");
                                for (int l = 0; l < parts.length; l++) {
                                    itemName += parts[l].toUpperCase();
                                    if (l < parts.length - 1) {
                                        itemName += "_";
                                    }
                                }
                                activeItem += itemName;
                                if (k < animalCard.getActiveItems().size() - 1) {
                                    activeItem += " ";
                                }
                            }
                            String name = "";
                            String parts[] = animalCard.getName().split(" ");
                            for (int k = 0; k < parts.length; k++) {
                                name += parts[k].toUpperCase();
                                if (k < parts.length - 1) {
                                    name += "_";
                                }
                            }
                            lines.add(fieldIndexToCode(new int[] { i, j }) + " " + name + " "
                                    + animalCard.getCurrentWeight() +
                                    " " + animalCard.getActiveItems().size() + " " + activeItem);

                        } else if (card instanceof PlantCardModel) {
                            PlantCardModel plantCard = (PlantCardModel) card;
                            String activeItem = "";
                            for (int k = 0; k < plantCard.getActiveItems().size(); k++) {
                                String itemName = "";
                                String parts[] = plantCard.getActiveItems().get(k).getName().split(" ");
                                for (int l = 0; l < parts.length; l++) {
                                    itemName += parts[l].toUpperCase();
                                    if (l < parts.length - 1) {
                                        itemName += "_";
                                    }
                                }
                                activeItem += itemName;
                                if (k < plantCard.getActiveItems().size() - 1) {
                                    activeItem += " ";
                                }
                            }
                            String name = "";
                            String parts[] = plantCard.getName().split(" ");
                            for (int k = 0; k < parts.length; k++) {
                                name += parts[k].toUpperCase();
                                if (k < parts.length - 1) {
                                    name += "_";
                                }
                            }
                            lines.add(fieldIndexToCode(new int[] { i, j }) + " " + name + " "
                                    + plantCard.getCurrentAge() +
                                    " " + plantCard.getActiveItems().size() + " " + activeItem);
                        }
                    }
                }
            }
            Files.write(playerPath, lines);
            Files.write(playerResourcePath, lines);
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
            e.printStackTrace();
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
