package com.if2210.app.plugin;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import com.if2210.app.factory.AnimalCardFactory;
import com.if2210.app.factory.ItemCardFactory;
import com.if2210.app.factory.PlantCardFactory;
import com.if2210.app.factory.ProductCardFactory;
import com.if2210.app.model.AnimalCardModel;
import com.if2210.app.model.CardModel;
import com.if2210.app.model.DeckModel;
import com.if2210.app.model.ItemCardModel;
import com.if2210.app.model.PlantCardModel;
import com.if2210.app.model.PlayerModel;
import com.if2210.app.model.ProductCardModel;

public class YAMLExtension {
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

    public YAMLExtension() {
        // try {
        //     this.path = Paths.get(getClass().getResource("/com/if2210/app/gamestates/state0").toURI());
        // } catch (URISyntaxException e) {
        //     e.printStackTrace();
        //     return;
        // }
        // this.gameStatePath = this.path.resolve("gamestate.yaml");
        // this.player1Path = this.path.resolve("player1.yaml");
        // this.player2Path = this.path.resolve("player2.yaml");

        // this.currentTurn = 0;
        // this.productList = new HashMap<ProductCardModel, Integer>();
    }

    public YAMLExtension(Path path) {
        this.path = path;
        this.gameStatePath = this.path.resolve("gamestate.yaml");
        this.player1Path = this.path.resolve("player1.yaml");
        this.player2Path = this.path.resolve("player2.yaml");

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
            if (url == null || Paths.get("src/main/resources/com/if2210/app/gamestates", folderName) == null) {
                Files.createDirectory(
                        Paths.get(getClass().getResource("/com/if2210/app/gamestates").toURI()).resolve(folderName));
                Files.createDirectory(
                        Paths.get("src/main/resources/com/if2210/app/gamestates", folderName));

                Files.createFile(Paths.get(getClass().getResource("/com/if2210/app/gamestates").toURI())
                        .resolve(folderName).resolve("gamestate.yaml"));
                Files.createFile(
                        Paths.get("src/main/resources/com/if2210/app/gamestates", folderName).resolve("gamestate.yaml"));

                Files.createFile(Paths.get(getClass().getResource("/com/if2210/app/gamestates").toURI())
                        .resolve(folderName).resolve("player1.yaml"));
                Files.createFile(
                        Paths.get("src/main/resources/com/if2210/app/gamestates", folderName).resolve("player1.yaml"));

                Files.createFile(Paths.get(getClass().getResource("/com/if2210/app/gamestates").toURI())
                        .resolve(folderName).resolve("player2.yaml"));
                Files.createFile(
                        Paths.get("src/main/resources/com/if2210/app/gamestates", folderName).resolve("player2.yaml"));
                url = getClass().getResource("/com/if2210/app/gamestates/" + folderName);
            }
            this.path = Paths.get(url.toURI());
            this.resourcePath = Paths.get("src/main/resources/com/if2210/app/gamestates", folderName);
            this.gameStatePath = this.path.resolve("gamestate.yaml");
            this.gameStateResourcePath = this.resourcePath.resolve("gamestate.yaml");
            this.player1Path = this.path.resolve("player1.yaml");
            this.player1ResourcePath = this.resourcePath.resolve("player1.yaml");
            this.player2Path = this.path.resolve("player2.yaml");
            this.player2ResourcePath = this.resourcePath.resolve("player2.yaml");
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
            Map<String, Object> data = new LinkedHashMap<>(); // Import necessary classes

            data.put("current_turn", Integer.toString(currentTurn));
            data.put("items_exist_in_shop", Integer.toString(productList.size()));

            List<Map<String, String>> items = new ArrayList<>();
            for (Map.Entry<ProductCardModel, Integer> entry : productList.entrySet()) {
                Map<String, String> item = new LinkedHashMap<>(); // Import necessary class
                String name = entry.getKey().getName().replace(" ", "_").toUpperCase();
                item.put("name", name);
                item.put("quantity", Integer.toString(entry.getValue()));
                items.add(item);
            }
            data.put("items", items);

            Yaml yaml = new Yaml();

            String yamlString = yaml.dump(data);
            // yamlString = "---\n" + yamlString;  // Add the document marker
            System.out.println(yamlString);
            System.out.println(gameStateResourcePath);
            Files.writeString(gameStatePath, yamlString);
            Files.writeString(gameStateResourcePath, yamlString);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void savePlayerData(PlayerModel player, Path playerPath, Path playerResourcePath) {
        try {
            Map<String, Object> data = new LinkedHashMap<>(); // Use LinkedHashMap to maintain insertion order
        
            // Add elements to the LinkedHashMap in the desired order
            data.put("gulden", + player.getMoney());
            data.put("cards_in_deck", + player.getDeck().getDeckSize());
            data.put("cards_exist_in_active_deck", + player.getActiveDeck().getEffectiveDeckSize());
        
            // Add active cards
            List<Map<String, String>> activeCards = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                if (player.getActiveDeck().getCard(i) != null) {
                    Map<String, String> cardData = new LinkedHashMap<>(); // Use LinkedHashMap to maintain insertion order
                    cardData.put("name", player.getActiveDeck().getCard(i).getName().replace(" ", "_").toUpperCase());
                    cardData.put("location", activeIndexToCode(i));
                    activeCards.add(cardData);
                }
            }
            data.put("active_cards", activeCards);
        
            // Add cards_exist_in_field
            data.put("cards_exist_in_field", player.getField().getEffectiveFieldSize());
        
            // Add field cards
            List<Map<String, Object>> fieldCards = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 5; j++) {
                    CardModel card = player.getField().getCard(i, j);
                    if (card != null) {
                        Map<String, Object> cardData = new LinkedHashMap<>(); // Use LinkedHashMap to maintain insertion order
                        cardData.put("name", card.getName().replace(" ", "_").toUpperCase());
                        cardData.put("location", fieldIndexToCode(new int[]{i, j}));
        
                        if (card instanceof AnimalCardModel) {
                            AnimalCardModel animalCard = (AnimalCardModel) card;
                            cardData.put("age_or_weight", animalCard.getCurrentWeight());
                            cardData.put("items_count", animalCard.getActiveItems().size());
                            List<String> activeItems = new ArrayList<>();
                            for (ItemCardModel item : animalCard.getActiveItems()) {
                                activeItems.add(item.getName().replace(" ", "_").toUpperCase());
                            }
                            cardData.put("items", activeItems);
                        } else if (card instanceof PlantCardModel) {
                            PlantCardModel plantCard = (PlantCardModel) card;
                            cardData.put("age_or_weight", plantCard.getCurrentAge());
                            cardData.put("items_count", plantCard.getActiveItems().size());
                            List<String> activeItems = new ArrayList<>();
                            for (ItemCardModel item : plantCard.getActiveItems()) {
                                activeItems.add(item.getName().replace(" ", "_").toUpperCase());
                            }
                            cardData.put("items", activeItems);
                        }
        
                        fieldCards.add(cardData);
                    }
                }
            }
            data.put("field_cards", fieldCards);
        
            // Construct YAML string
            Yaml yaml = new Yaml();
            String yamlString = yaml.dump(data);
        
            // Write YAML string to files
            Files.writeString(playerPath, yamlString);
            Files.writeString(playerResourcePath, yamlString);
        
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // @Override
    public void load(String folderName) {
        try {
            URL url = getClass().getResource("/com/if2210/app/gamestates/" + folderName);
            if (url == null) {
                throw new IllegalArgumentException("Folder doesn't exist");
            }
            this.path = Paths.get(url.toURI());
            this.gameStatePath = this.path.resolve("gamestate.yaml");
            this.player1Path = this.path.resolve("player1.yaml");
            this.player2Path = this.path.resolve("player2.yaml");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }


        loadGameState();


        try {
            this.player1 = new PlayerModel();
            Yaml yaml = new Yaml();
            String yamlContent = Files.readString(player1Path);
            Map<String, Object> data = yaml.load(yamlContent);

            int gulden = (int) data.get("gulden");
            int deckCount = (int) data.get("cards_in_deck");
            DeckModel deck = new DeckModel(deckCount);


            // Parsing active deck
            List<Map<String, String>> activeCards = (List<Map<String, String>>) data.get("active_cards");
            for (Map<String, String> cardData : activeCards) {
                String name = cardData.get("name");
                int index = codeToActiveIndex(cardData.get("location"));

                if (isAnimalCard(name) != null) {
                    AnimalCardModel card = AnimalCardFactory.createAnimalCard(isAnimalCard(name));
                    this.player1.getActiveDeck().setCard(index, card);
                } else if (isPlantCard(name) != null) {
                    PlantCardModel card = PlantCardFactory.createPlantCard(isPlantCard(name));
                    this.player1.getActiveDeck().setCard(index, card);
                } else if (isProductCard(name) != null) {
                    ProductCardModel card = ProductCardFactory.createProductCard(isProductCard(name));
                    this.player1.getDeck().getCards().add(card);
                } else if (isItemCard(name) != null) {
                    ItemCardModel card = ItemCardFactory.createItemCard(isItemCard(name));
                    this.player1.getDeck().getCards().add(card);
                }
            }


            List<Map<String, Object>> fieldCards = (List<Map<String, Object>>) data.get("field_cards");
            for (Map<String, Object> cardData : fieldCards) {
                String name = (String) cardData.get("name");
                int[] index = codeToFieldIndex((String) cardData.get("location"));
                int ageOrWeight = (int) cardData.get("age_or_weight"); // Directly get integer value
                int itemsCount = (int) cardData.get("items_count"); // Directly get integer value
                List<String> items = (List<String>) cardData.get("items");

                System.out.println(this.player1.getField().getEffectiveFieldSize());
                if (isAnimalCard(name) != null) {
                    System.out.println("Animal card: " + name);
                    AnimalCardModel card = AnimalCardFactory.createAnimalCard(isAnimalCard(name));
                    card.setCurrentWeight(ageOrWeight);
                    for (String itemName : items) {
                        card.getActiveItems().add(ItemCardFactory.createItemCard(itemName));
                    }
                    this.player1.getField().setCard(card, index[0], index[1]);
                    System.out.println(this.player1.getField().getEffectiveFieldSize());
                } else if (isPlantCard(name) != null) {
                    System.out.println("Animal card: " + name);
                    PlantCardModel card = PlantCardFactory.createPlantCard(isPlantCard(name));
                    card.setCurrentAge(ageOrWeight);
                    for (String itemName : items) {
                        card.getActiveItems().add(ItemCardFactory.createItemCard(itemName));
                    }
                    this.player1.getField().setCard(card, index[0], index[1]);
                }
                System.out.println(this.player1.getField().getEffectiveFieldSize());
            }

            // Set other player data
            this.player1.setMoney(gulden);
            this.player1.setDeck(deck);

        } catch (IOException e) {
            e.printStackTrace();
        }



        try {
            this.player2 = new PlayerModel();
            Yaml yaml = new Yaml();
            String yamlContent = Files.readString(player2Path);
            Map<String, Object> data = yaml.load(yamlContent);

            int gulden = (int) data.get("gulden");
            int deckCount = (int) data.get("cards_in_deck");
            DeckModel deck = new DeckModel(deckCount);


            // Parsing active deck
            List<Map<String, String>> activeCards = (List<Map<String, String>>) data.get("active_cards");
            for (Map<String, String> cardData : activeCards) {
                String name = cardData.get("name");
                int index = codeToActiveIndex(cardData.get("location"));

                if (isAnimalCard(name) != null) {
                    AnimalCardModel card = AnimalCardFactory.createAnimalCard(isAnimalCard(name));
                    this.player2.getActiveDeck().setCard(index, card);
                } else if (isPlantCard(name) != null) {
                    PlantCardModel card = PlantCardFactory.createPlantCard(isPlantCard(name));
                    this.player2.getActiveDeck().setCard(index, card);
                } else if (isProductCard(name) != null) {
                    ProductCardModel card = ProductCardFactory.createProductCard(isProductCard(name));
                    this.player2.getDeck().getCards().add(card);
                } else if (isItemCard(name) != null) {
                    ItemCardModel card = ItemCardFactory.createItemCard(isItemCard(name));
                    this.player2.getDeck().getCards().add(card);
                }
            }


            List<Map<String, Object>> fieldCards = (List<Map<String, Object>>) data.get("field_cards");
            for (Map<String, Object> cardData : fieldCards) {
                String name = (String) cardData.get("name");
                int[] index = codeToFieldIndex((String) cardData.get("location"));
                int ageOrWeight = (int) cardData.get("age_or_weight"); // Directly get integer value
                int itemsCount = (int) cardData.get("items_count"); // Directly get integer value
                List<String> items = (List<String>) cardData.get("items");

                if (isAnimalCard(name) != null) {
                    AnimalCardModel card = AnimalCardFactory.createAnimalCard(isAnimalCard(name));
                    card.setCurrentWeight(ageOrWeight);
                    for (String itemName : items) {
                        card.getActiveItems().add(ItemCardFactory.createItemCard(itemName));
                    }
                    this.player2.getField().setCard(card, index[0], index[1]);
                } else if (isPlantCard(name) != null) {
                    PlantCardModel card = PlantCardFactory.createPlantCard(isPlantCard(name));
                    card.setCurrentAge(ageOrWeight);
                    for (String itemName : items) {
                        card.getActiveItems().add(ItemCardFactory.createItemCard(itemName));
                    }
                    this.player2.getField().setCard(card, index[0], index[1]);
                }
            }

            // Set other player data
            this.player2.setMoney(gulden);
            this.player2.setDeck(deck);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadGameState() {
        try {
            Yaml yaml = new Yaml();
            String yamlContent = Files.readString(gameStatePath);
            Map<String, Object> data = yaml.load(yamlContent);

            this.currentTurn = Integer.parseInt((String) data.get("current_turn"));

            // Commented out because the itemCount is not used in the provided YAML format
            // int itemCount = Integer.parseInt((String) data.get("items_exist_in_shop"));

            List<Map<String, String>> items = (List<Map<String, String>>) data.get("items");
            for (Map<String, String> itemData : items) {
                String name = itemData.get("name");
                int quantity = Integer.parseInt(itemData.get("quantity"));

                ProductCardModel product = ProductCardFactory.createProductCard(isProductCard(name));
                this.productList.put(product, quantity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
