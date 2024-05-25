package com.if2210.app.plugin;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.if2210.app.datastore.DataManager;
import com.if2210.app.factory.AnimalCardFactory;
import com.if2210.app.factory.ItemCardFactory;
import com.if2210.app.factory.PlantCardFactory;
import com.if2210.app.factory.ProductCardFactory;
import com.if2210.app.model.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class JSONExtension implements Extension {
    private Path path;
    private Path gameStatePath;
    private Path player1Path;
    private Path player2Path;

    private int currentTurn;
    private Map<ProductCardModel, Integer> productList;
    private PlayerModel player1;
    private PlayerModel player2;

    public JSONExtension() {
        try {
            this.path = Paths.get(getClass().getResource("/com/if2210/app/gamestates/state0").toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        this.gameStatePath = this.path.resolve("gamestate.json");
        this.player1Path = this.path.resolve("player1.json");
        this.player2Path = this.path.resolve("player2.json");

        this.currentTurn = 0;
        this.productList = new HashMap<ProductCardModel, Integer>();
    }

    public JSONExtension(Path path) {
        this.path = path;
        this.gameStatePath = this.path.resolve("gamestate.json");
        this.player1Path = this.path.resolve("player1.json");
        this.player2Path = this.path.resolve("player2.json");

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
            Path basePath = Paths.get("src/main/resources/com/if2210/app/gamestates", folderName);
            if (!Files.exists(basePath)) {
                Files.createDirectories(basePath);
            }

            Path gameStatePath = basePath.resolve("gamestate.json");
            Path player1Path = basePath.resolve("player1.json");
            Path player2Path = basePath.resolve("player2.json");

            saveGameState(gameStatePath, currentTurn, productList);
            savePlayerData(player1, player1Path);
            savePlayerData(player2, player2Path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveGameState(Path path, int currentTurn, Map<ProductCardModel, Integer> productList) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.createObjectNode();

        rootNode.put("current_turn", currentTurn);

        rootNode.put("items_exist_in_shop", productList.size());

        ArrayNode productListNode = rootNode.putArray("items");
        for (Map.Entry<ProductCardModel, Integer> entry : productList.entrySet()) {
            ObjectNode productNode = objectMapper.createObjectNode();
            String nameSave = replaceSpaceIfTwoWords(entry.getKey().getName().toUpperCase());
            productNode.put("name", nameSave);
            productNode.put("quantity", entry.getValue());
            productListNode.add(productNode);
        }

        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), rootNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void savePlayerData(PlayerModel player, Path path) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.createObjectNode();

        rootNode.put("gulden", player.getMoney());
        rootNode.put("cards_in_deck", player.getDeck().getDeckSize());
        rootNode.put("cards_exist_in_active_deck", player.getActiveDeck().getEffectiveDeckSize());

        ArrayNode activeDeckNode = rootNode.putArray("active_cards");
        for (int i = 0; i < 6; i++) {
            CardModel card = player.getActiveDeck().getCard(i);
            if (card != null) {
                ObjectNode cardNode = objectMapper.createObjectNode();
                cardNode.put("location", activeIndexToCode(i));
                String nameSave = replaceSpaceIfTwoWords(card.getName().toUpperCase());
                cardNode.put("name", nameSave);
                
                activeDeckNode.add(cardNode);
            }
        }

        rootNode.put("cards_exist_in_field", player.getField().getEffectiveFieldSize());

        ArrayNode fieldCardsNode = rootNode.putArray("field_cards");
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                CardModel card = player.getField().getCard(i, j);
                if (card != null) {
                    ObjectNode cardNode = objectMapper.createObjectNode();
                    cardNode.put("location", fieldIndexToCode(new int[]{i, j}));
                    String nameSave = replaceSpaceIfTwoWords(card.getName().toUpperCase());
                    cardNode.put("name", nameSave);

                    if (card instanceof AnimalCardModel) {
                        AnimalCardModel animalCard = (AnimalCardModel) card;
                        cardNode.put("age_or_weight", animalCard.getCurrentWeight());
                        cardNode.put("items_count", animalCard.getActiveItems().size());

                        ArrayNode itemsNode = cardNode.putArray("items");
                        for (ItemCardModel item : animalCard.getActiveItems()) {
                            if (item != null) {
                                itemsNode.add(item.getName().toUpperCase());
                            }
                        }
                    } else if (card instanceof PlantCardModel) {
                        PlantCardModel plantCard = (PlantCardModel) card;
                        cardNode.put("age_or_weight", plantCard.getCurrentAge());
                        cardNode.put("items_count", plantCard.getActiveItems().size());

                        ArrayNode itemsNode = cardNode.putArray("items");
                        for (ItemCardModel item : plantCard.getActiveItems()) {
                            if (item != null) {
                                itemsNode.add(item.getName().toUpperCase());
                            }
                        }
                    }

                    fieldCardsNode.add(cardNode);
                }
            }
        }

        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), rootNode);
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
            this.gameStatePath = this.path.resolve("gamestate.json");
            this.player1Path = this.path.resolve("player1.json");
            this.player2Path = this.path.resolve("player2.json");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        
        loadGameState();
        
        // loadPlayerData(player1Path, 1);
        try {
            this.player1 = new PlayerModel();
            
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(Files.newBufferedReader(player1Path));
            int gulden = rootNode.get("gulden").asInt();
            int cards_in_deck = rootNode.get("cards_in_deck").asInt();
            int cards_exist_in_active_deck = rootNode.get("cards_exist_in_active_deck").asInt();
            DeckModel deck = new DeckModel(cards_in_deck);
            
            this.player1.setMoney(gulden);
            this.player1.setDeck(deck);
            
            JsonNode active_cards_node = rootNode.get("active_cards");
            for (JsonNode cardNode : active_cards_node) {
                String cardName = cardNode.get("name").asText();
                String animalCard = isAnimalCard(cardName);
                String plantCard = isPlantCard(cardName);
                String productCard = isProductCard(cardName);
                String itemCard = isItemCard(cardName);
                int index = codeToActiveIndex(cardNode.get("location").asText());
                
                if (animalCard != null) {
                    AnimalCardModel animalCardModel = AnimalCardFactory.createAnimalCard(animalCard);
                    this.player1.getActiveDeck().setCard(index, animalCardModel);
                } else if (plantCard != null) {
                    PlantCardModel plantCardModel = PlantCardFactory.createPlantCard(plantCard);
                    this.player1.getActiveDeck().setCard(index, plantCardModel);
                } else if (productCard != null) {
                    ProductCardModel productCardModel = ProductCardFactory.createProductCard(productCard);
                    this.player1.getActiveDeck().setCard(index, productCardModel);
                } else if (itemCard != null) {
                    ItemCardModel itemCardModel = ItemCardFactory.createItemCard(itemCard);
                    this.player1.getActiveDeck().setCard(index, itemCardModel);
                }
            }
            
            int cards_exist_in_field = rootNode.get("cards_exist_in_field").asInt();
            
            JsonNode fieldNode = rootNode.get("field_cards");
            for (JsonNode cardNode : fieldNode) {
                String cardName = cardNode.get("name").asText();
                
                String animalCard = isAnimalCard(cardName);
                String plantCard = isPlantCard(cardName);
                
                int[] index = codeToFieldIndex(cardNode.get("location").asText());
                int age_or_weight = cardNode.get("age_or_weight").asInt();
                
                if (animalCard != null) {
                    AnimalCardModel animalCardModel = AnimalCardFactory.createAnimalCard(animalCard);
                    animalCardModel.setCurrentWeight(age_or_weight);
                    JsonNode itemsNode = cardNode.get("items");
                    for (JsonNode item : itemsNode) {
                        String cardItemName = item.asText();
                        animalCardModel.getActiveItems().add(ItemCardFactory.createItemCard(cardItemName));
                    }
                    this.player1.getField().setCard(animalCardModel, index[0], index[1]);
                } else if (plantCard != null) {
                    PlantCardModel plantCardModel = PlantCardFactory.createPlantCard(plantCard);
                    plantCardModel.setCurrentAge(age_or_weight);
                    JsonNode itemsNode = cardNode.get("items");
                    for (JsonNode item : itemsNode) {
                        String cardItemName = item.asText();
                        plantCardModel.getActiveItems().add(ItemCardFactory.createItemCard(cardItemName));
                    }
                    this.player1.getField().setCard(plantCardModel, index[0], index[1]);
                }                 
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }
        
        
        // loadPlayerData(player2Path, 2);
        try {
            this.player2 = new PlayerModel();
            
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(Files.newBufferedReader(player1Path));
            int gulden = rootNode.get("gulden").asInt();
            int cards_in_deck = rootNode.get("cards_in_deck").asInt();
            int cards_exist_in_active_deck = rootNode.get("cards_exist_in_active_deck").asInt();
            DeckModel deck = new DeckModel(cards_in_deck);

            this.player2.setMoney(gulden);
            this.player2.setDeck(deck);

            JsonNode active_cards_node = rootNode.get("active_cards");
            for (JsonNode cardNode : active_cards_node) {
                String cardName = cardNode.get("name").asText();
                String animalCard = isAnimalCard(cardName);
                String plantCard = isPlantCard(cardName);
                String productCard = isProductCard(cardName);
                String itemCard = isItemCard(cardName);
                int index = codeToActiveIndex(cardNode.get("location").asText());

                if (animalCard != null) {
                    AnimalCardModel animalCardModel = AnimalCardFactory.createAnimalCard(animalCard);
                    this.player2.getActiveDeck().setCard(index, animalCardModel);
                } else if (plantCard != null) {
                    PlantCardModel plantCardModel = PlantCardFactory.createPlantCard(plantCard);
                    this.player2.getActiveDeck().setCard(index, plantCardModel);
                } else if (productCard != null) {
                    ProductCardModel productCardModel = ProductCardFactory.createProductCard(productCard);
                    this.player2.getActiveDeck().setCard(index, productCardModel);
                } else if (itemCard != null) {
                    ItemCardModel itemCardModel = ItemCardFactory.createItemCard(itemCard);
                    this.player2.getActiveDeck().setCard(index, itemCardModel);
                }
            }

            int cards_exist_in_field = rootNode.get("cards_exist_in_field").asInt();

            JsonNode fieldNode = rootNode.get("field_cards");
            for (JsonNode cardNode : fieldNode) {
                String cardName = cardNode.get("name").asText();

                String animalCard = isAnimalCard(cardName);
                String plantCard = isPlantCard(cardName);

                int[] index = codeToFieldIndex(cardNode.get("location").asText());
                int age_or_weight = cardNode.get("age_or_weight").asInt();

                if (animalCard != null) {
                    AnimalCardModel animalCardModel = AnimalCardFactory.createAnimalCard(animalCard);
                    animalCardModel.setCurrentWeight(age_or_weight);
                    JsonNode itemsNode = cardNode.get("items");
                    for (JsonNode item : itemsNode) {
                        String cardItemName = item.asText();
                        animalCardModel.getActiveItems().add(ItemCardFactory.createItemCard(cardItemName));
                    }
                    this.player2.getField().setCard(animalCardModel, index[0], index[1]);
                } else if (plantCard != null) {
                    PlantCardModel plantCardModel = PlantCardFactory.createPlantCard(plantCard);
                    plantCardModel.setCurrentAge(age_or_weight);
                    JsonNode itemsNode = cardNode.get("items");
                    for (JsonNode item : itemsNode) {
                        String cardItemName = item.asText();
                        plantCardModel.getActiveItems().add(ItemCardFactory.createItemCard(cardItemName));
                    }
                    this.player2.getField().setCard(plantCardModel, index[0], index[1]);
                }                 
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    private void loadGameState() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(Files.newBufferedReader(gameStatePath));
            this.currentTurn = rootNode.get("current_turn").asInt();
            int itemCount = rootNode.get("items_exist_in_shop").asInt();
            
            JsonNode itemsNode = rootNode.get("items");
            for (JsonNode itemNode : itemsNode) {
                String itemName = itemNode.get("name").asText();
                int quantity = itemNode.get("quantity").asInt();
                ProductCardModel product = ProductCardFactory.createProductCard(isProductCard(itemName));
                this.productList.put(product, quantity);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    public String replaceSpaceIfTwoWords(String input) {
        if (input == null || input.isEmpty()) {
            return input; // Return the original string if it is null or empty
        }

        String[] words = input.trim().split("\\s+"); // Split the string by spaces
        if (words.length == 2) {
            return input.replace(" ", "_"); // Replace space with underscore if exactly two words
        }
        return input; // Return the original string if not exactly two words
    }
}