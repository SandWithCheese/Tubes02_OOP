// package com.if2210.app.plugin;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.if2210.app.datastore.DataManager;
// import com.if2210.app.factory.AnimalCardFactory;
// import com.if2210.app.factory.ItemCardFactory;
// import com.if2210.app.factory.ProductCardFactory;
// import com.if2210.app.model.*;

// import java.io.IOException;
// import java.net.URISyntaxException;
// import java.net.URL;
// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.nio.file.Paths;
// import java.util.*;

// public class JSONExtension implements Extension {
//     private Path path;
//     private Path gameStatePath;
//     private Path player1Path;
//     private Path player2Path;

//     private int currentTurn;
//     private Map<ProductCardModel, Integer> productList;
//     private PlayerModel player1;
//     private PlayerModel player2;

//     private ObjectMapper mapper;

//     public JSONExtension() {
//         try {
//             this.path = Paths.get(getClass().getResource("/com/if2210/app/gamestates/state0").toURI());
//         } catch (URISyntaxException e) {
//             e.printStackTrace();
//             return;
//         }
//         this.gameStatePath = this.path.resolve("gamestate.json");
//         this.player1Path = this.path.resolve("player1.json");
//         this.player2Path = this.path.resolve("player2.json");

//         this.currentTurn = 0;
//         this.productList = new HashMap<>();
//         this.mapper = new ObjectMapper();
//     }

//     public JSONExtension(Path path) {
//         this.path = path;
//         this.gameStatePath = this.path.resolve("gamestate.json");
//         this.player1Path = this.path.resolve("player1.json");
//         this.player2Path = this.path.resolve("player2.json");

//         this.currentTurn = 0;
//         this.productList = new HashMap<>();
//         this.mapper = new ObjectMapper();
//     }

//     public Path getPath() {
//         return path;
//     }

//     public Path getGameStatePath() {
//         return gameStatePath;
//     }

//     public int getCurrentTurn() {
//         return currentTurn;
//     }

//     public void setCurrentTurn(int currentTurn) {
//         this.currentTurn = currentTurn;
//     }

//     public PlayerModel getPlayer1() {
//         return player1;
//     }

//     public void setPlayer1(PlayerModel player1) {
//         this.player1 = player1;
//     }

//     public PlayerModel getPlayer2() {
//         return player2;
//     }

//     public void setPlayer2(PlayerModel player2) {
//         this.player2 = player2;
//     }

//     public Map<ProductCardModel, Integer> getProductList() {
//         return productList;
//     }

//     public void setProductList(Map<ProductCardModel, Integer> productList) {
//         this.productList = productList;
//     }

//     @Override
//     public void save(String folderName, int currentTurn, Map<ProductCardModel, Integer> productList,
//                      PlayerModel player1, PlayerModel player2) {
//         try {
//             URL url = getClass().getResource("/com/if2210/app/gamestates/" + folderName);
//             if (url == null || Paths.get("src/main/resources/com/if2210/app/gamestates", folderName) == null) {
//                 Files.createDirectories(
//                         Paths.get(getClass().getResource("/com/if2210/app/gamestates").toURI()).resolve(folderName));
//                 url = getClass().getResource("/com/if2210/app/gamestates/" + folderName);
//             }
//             this.path = Paths.get(url.toURI());
//             this.gameStatePath = this.path.resolve("gamestate.json");
//             this.player1Path = this.path.resolve("player1.json");
//             this.player2Path = this.path.resolve("player2.json");
//         } catch (URISyntaxException | IOException e) {
//             e.printStackTrace();
//             return;
//         }

//         saveGameState(currentTurn, productList);
//         savePlayerData(player1, player1Path);
//         savePlayerData(player2, player2Path);
//     }

//     private void saveGameState(int currentTurn, Map<ProductCardModel, Integer> productList) {
//         try {
//             Map<String, Object> gameState = new HashMap<>();
//             gameState.put("currentTurn", currentTurn);
//             List<Map<String, Object>> products = new ArrayList<>();
//             for (Map.Entry<ProductCardModel, Integer> entry : productList.entrySet()) {
//                 Map<String, Object> productData = new HashMap<>();
//                 productData.put("name", entry.getKey().getName());
//                 productData.put("count", entry.getValue());
//                 products.add(productData);
//             }
//             gameState.put("products", products);
//             mapper.writeValue(gameStatePath.toFile(), gameState);
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//     }

//     private void savePlayerData(PlayerModel player, Path playerPath) {
//         try {
//             Map<String, Object> playerData = new HashMap<>();
//             playerData.put("money", player.getMoney());
//             playerData.put("deckSize", player.getDeck().getDeckSize());
//             playerData.put("activeDeckSize", player.getActiveDeck().getEffectiveDeckSize());
//             List<Map<String, String>> activeDeck = new ArrayList<>();
//             for (int i = 0; i < 6; i++) {
//                 CardModel card = player.getActiveDeck().getCard(i);
//                 if (card != null) {
//                     Map<String, String> cardData = new HashMap<>();
//                     cardData.put("code", activeIndexToCode(i));
//                     cardData.put("name", card.getName());
//                     activeDeck.add(cardData);
//                 }
//             }
//             playerData.put("activeDeck", activeDeck);
//             playerData.put("fieldSize", player.getField().getEffectiveFieldSize());
//             List<Map<String, Object>> field = new ArrayList<>();
//             for (int i = 0; i < 4; i++) {
//                 for (int j = 0; j < 5; j++) {
//                     CardModel card = player.getField().getCard(i, j);
//                     if (card != null) {
//                         Map<String, Object> cardData = new HashMap<>();
//                         cardData.put("code", fieldIndexToCode(new int[]{i, j}));
//                         cardData.put("name", card.getName());
//                         if (card instanceof AnimalCardModel) {
//                             AnimalCardModel animalCard = (AnimalCardModel) card;
//                             cardData.put("currentWeight", animalCard.getCurrentWeight());
//                             List<String> activeItems = new ArrayList<>();
//                             for (ItemCardModel item : animalCard.getActiveItems()) {
//                                 activeItems.add(item.getName());
//                             }
//                             cardData.put("activeItems", activeItems);
//                         } else if (card instanceof PlantCardModel) {
//                             PlantCardModel plantCard = (PlantCardModel) card;
//                             cardData.put("currentAge", plantCard.getCurrentAge());
//                             List<String> activeItems = new ArrayList<>();
//                             for (ItemCardModel item : plantCard.getActiveItems()) {
//                                 activeItems.add(item.getName());
//                             }
//                             cardData.put("activeItems", activeItems);
//                         }
//                         field.add(cardData);
//                     }
//                 }
//             }
//             playerData.put("field", field);
//             mapper.writeValue(playerPath.toFile(), playerData);
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//     }

//     @Override
//     public void load(String folderName) {
//         try {
//             URL url = getClass().getResource("/com/if2210/app/gamestates/" + folderName);
//             if (url == null) {
//                 throw new IllegalArgumentException("Folder doesn't exist");
//             }
//             this.path = Paths.get(url.toURI());
//             this.gameStatePath = this.path.resolve("gamestate.json");
//             this.player1Path = this.path.resolve("player1.json");
//             this.player2Path = this.path.resolve("player2.json");
//         } catch (URISyntaxException e) {
//             e.printStackTrace();
//             return;
//         }

//         loadGameState();
//         this.player1 = loadPlayerData(player1Path);
//         this.player2 = loadPlayerData(player2Path);
//     }

//     private void loadGameState() {
//         try {
//             Map<String, Object> gameState = mapper.readValue(gameStatePath.toFile(), Map.class);
//             this.currentTurn = (Integer) gameState.get("currentTurn");
//             List<Map<String, Object>> products = (List<Map<String, Object>>) gameState.get("products");
//             for (Map<String, Object> productData : products) {
//                 String name = (String) productData.get("name");
//                 int count = (Integer) productData.get("count");
//                 ProductCardModel product = ProductCardFactory.createProductCard(name);
//                 this.productList.put(product, count);
//             }
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//     }

//     private PlayerModel loadPlayerData(Path playerPath) {
//         try {
//             Map<String, Object> playerData = mapper.readValue(playerPath.toFile(), Map.class);
//             int money = (Integer) playerData.get("money");
//             int deckSize = (Integer) playerData.get("deckSize");
//             int activeDeckSize = (Integer) playerData.get("activeDeckSize");

//             DeckModel deck = new DeckModel(deckSize);
//             ActiveDeckModel activeDeck = new ActiveDeckModel();
//             List<Map<String, String>> activeDeckData = (List<Map<String, String>>) playerData.get("activeDeck");
//             for (Map<String, String> cardData : activeDeckData) {
//                 String code = cardData.get("code");
//                 String name = cardData.get("name");
//                 int index = activeCodeToIndex(code);
//                 // CardModel card = CardModelFactory.create(name);
//                 activeDeck.setCard(index, card);
//             }
//             FieldModel field = new FieldModel();
//             List<Map<String, Object>> fieldData = (List<Map<String, Object>>) playerData.get("field");
//             for (Map<String, Object> cardData : fieldData) {
//                 String code = (String) cardData.get("code");
//                 String name = (String) cardData.get("name");
//                 int[] index = fieldCodeToIndex(code);
//                 // CardModel card = CardModelFactory.create(name);
//                 if (card instanceof AnimalCardModel) {
//                     AnimalCardModel animalCard = (AnimalCardModel) card;
//                     animalCard.setCurrentWeight((Integer) cardData.get("currentWeight"));
//                     List<String> activeItems = (List<String>) cardData.get("activeItems");
//                     for (String itemName : activeItems) {
//                         ItemCardModel item = ItemCardFactory.create(itemName);
//                         animalCard.addItem(item);
//                     }
//                     field.setCard(index[0], index[1], animalCard);
//                 } else if (card instanceof PlantCardModel) {
//                     PlantCardModel plantCard = (PlantCardModel) card;
//                     plantCard.setCurrentAge((Integer) cardData.get("currentAge"));
//                     List<String> activeItems = (List<String>) cardData.get("activeItems");
//                     for (String itemName : activeItems) {
//                         ItemCardModel item = ItemCardFactory.create(itemName);
//                         plantCard.addItem(item);
//                     }
//                     field.setCard(index[0], index[1], plantCard);
//                 }
//             }

//             return new PlayerModel(field, activeDeck, deck, money);
//         } catch (IOException e) {
//             e.printStackTrace();
//             return null;
//         }
//     }

//     private String activeIndexToCode(int index) {
//         return String.valueOf((char) ('A' + index));
//     }

//     private int activeCodeToIndex(String code) {
//         return code.charAt(0) - 'A';
//     }

//     private String fieldIndexToCode(int[] index) {
//         return String.valueOf((char) ('A' + index[0])) + (index[1] + 1);
//     }

//     private int[] fieldCodeToIndex(String code) {
//         return new int[]{code.charAt(0) - 'A', Character.getNumericValue(code.charAt(1)) - 1};
//     }
// }
