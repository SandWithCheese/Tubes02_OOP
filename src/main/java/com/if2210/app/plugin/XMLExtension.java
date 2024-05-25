package com.if2210.app.plugin;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.if2210.app.datastore.DataManager;
import com.if2210.app.factory.AnimalCardFactory;
import com.if2210.app.factory.ItemCardFactory;
import com.if2210.app.factory.ProductCardFactory;
import com.if2210.app.model.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class XMLExtension implements Extension {
    private Path path;
    private Path gameStatePath;
    private Path player1Path;
    private Path player2Path;

    private int currentTurn;
    private Map<ProductCardModel, Integer> productList;
    private PlayerModel player1;
    private PlayerModel player2;

    private XmlMapper xmlMapper;

    public XMLExtension() {
        try {
            this.path = Paths.get(getClass().getResource("/com/if2210/app/gamestates/state0").toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        this.gameStatePath = this.path.resolve("gamestate.xml");
        this.player1Path = this.path.resolve("player1.xml");
        this.player2Path = this.path.resolve("player2.xml");

        this.currentTurn = 0;
        this.productList = new HashMap<>();
        this.xmlMapper = new XmlMapper();
    }

    public XMLExtension(Path path) {
        this.path = path;
        this.gameStatePath = this.path.resolve("gamestate.xml");
        this.player1Path = this.path.resolve("player1.xml");
        this.player2Path = this.path.resolve("player2.xml");

        this.currentTurn = 0;
        this.productList = new HashMap<>();
        this.xmlMapper = new XmlMapper();
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

    @Override
    public void save(String folderName, int currentTurn, Map<ProductCardModel, Integer> productList,
                     PlayerModel player1, PlayerModel player2) {
        try {
            URL url = getClass().getResource("/com/if2210/app/gamestates/" + folderName);
            if (url == null || Paths.get("src/main/resources/com/if2210/app/gamestates", folderName) == null) {
                Files.createDirectories(
                        Paths.get(getClass().getResource("/com/if2210/app/gamestates").toURI()).resolve(folderName));
                url = getClass().getResource("/com/if2210/app/gamestates/" + folderName);
            }
            this.path = Paths.get(url.toURI());
            this.gameStatePath = this.path.resolve("gamestate.xml");
            this.player1Path = this.path.resolve("player1.xml");
            this.player2Path = this.path.resolve("player2.xml");
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
            GameState gameState = new GameState();
            gameState.setCurrentTurn(currentTurn);
            List<ProductEntry> products = new ArrayList<>();
            for (Map.Entry<ProductCardModel, Integer> entry : productList.entrySet()) {
                products.add(new ProductEntry(entry.getKey().getName(), entry.getValue()));
            }
            gameState.setProducts(products);
            xmlMapper.writeValue(gameStatePath.toFile(), gameState);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void savePlayerData(PlayerModel player, Path playerPath) {
        try {
            PlayerData playerData = new PlayerData();
            playerData.setMoney(player.getMoney());
            playerData.setDeckSize(player.getDeck().getDeckSize());
            playerData.setActiveDeckSize(player.getActiveDeck().getEffectiveDeckSize());
            List<ActiveCardData> activeDeck = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                CardModel card = player.getActiveDeck().getCard(i);
                if (card != null) {
                    activeDeck.add(new ActiveCardData(activeIndexToCode(i), card.getName()));
                }
            }
            playerData.setActiveDeck(activeDeck);
            playerData.setFieldSize(player.getField().getEffectiveFieldSize());
            List<FieldCardData> field = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 5; j++) {
                    CardModel card = player.getField().getCard(i, j);
                    if (card != null) {
                        FieldCardData cardData = new FieldCardData(fieldIndexToCode(new int[]{i, j}), card.getName());
                        if (card instanceof AnimalCardModel) {
                            AnimalCardModel animalCard = (AnimalCardModel) card;
                            cardData.setCurrentWeight(animalCard.getCurrentWeight());
                            List<String> activeItems = new ArrayList<>();
                            for (ItemCardModel item : animalCard.getActiveItems()) {
                                activeItems.add(item.getName());
                            }
                            cardData.setActiveItems(activeItems);
                        } else if (card instanceof PlantCardModel) {
                            PlantCardModel plantCard = (PlantCardModel) card;
                            cardData.setCurrentAge(plantCard.getCurrentAge());
                            List<String> activeItems = new ArrayList<>();
                            for (ItemCardModel item : plantCard.getActiveItems()) {
                                activeItems.add(item.getName());
                            }
                            cardData.setActiveItems(activeItems);
                        }
                        field.add(cardData);
                    }
                }
            }
            playerData.setField(field);
            xmlMapper.writeValue(playerPath.toFile(), playerData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void load(String folderName) {
        try {
            URL url = getClass().getResource("/com/if2210/app/gamestates/" + folderName);
            if (url == null) {
                throw new IllegalArgumentException("Folder doesn't exist");
            }
            this.path = Paths.get(url.toURI());
            this.gameStatePath = this.path.resolve("gamestate.xml");
            this.player1Path = this.path.resolve("player1.xml");
            this.player2Path = this.path.resolve("player2.xml");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        loadGameState();
        this.player1 = loadPlayerData(player1Path);
        this.player2 = loadPlayerData(player2Path);
    }

    private void loadGameState() {
        try {
            GameState gameState = xmlMapper.readValue(gameStatePath.toFile(), GameState.class);
            this.currentTurn = gameState.getCurrentTurn();
            for (ProductEntry productEntry : gameState.getProducts()) {
                ProductCardModel product = ProductCardFactory.create(productEntry.getName());
                this.productList.put(product, productEntry.getCount());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private PlayerModel loadPlayerData(Path playerPath) {
        try {
            PlayerData playerData = xmlMapper.readValue(playerPath.toFile(), PlayerData.class);
            int money = playerData.getMoney();
            int deckSize = playerData.getDeckSize();
            int activeDeckSize = playerData.getActiveDeckSize();

            DeckModel deck = new DeckModel(deckSize);
            ActiveDeckModel activeDeck = new ActiveDeckModel();
            for (ActiveCardData cardData : playerData.getActiveDeck()) {
                String code = cardData.getCode();
                String name = cardData.getName();
                int index = activeCodeToIndex(code);
                CardModel card = CardModelFactory.create(name);
                activeDeck.setCard(index, card);
            }
            FieldModel field = new FieldModel();
            for (FieldCardData cardData : playerData.getField()) {
                String code = cardData.getCode();
                String name = cardData.getName();
                int[] index = fieldCodeToIndex(code);
                CardModel card = CardModelFactory.create(name);
                if (card instanceof AnimalCardModel) {
                    AnimalCardModel animalCard = (AnimalCardModel) card;
                    animalCard.setCurrentWeight(cardData.getCurrentWeight());
                    for (String itemName : cardData.getActiveItems()) {
                        ItemCardModel item = ItemCardFactory.create(itemName);
                        animalCard.addItem(item);
                    }
                    field.setCard(index[0], index[1], animalCard);
                } else if (card instanceof PlantCardModel) {
                    PlantCardModel plantCard = (PlantCardModel) card;
                    plantCard.setCurrentAge(cardData.getCurrentAge());
                    for (String itemName : cardData.getActiveItems()) {
                        ItemCardModel item = ItemCardFactory.create(itemName);
                        plantCard.addItem(item);
                    }
                    field.setCard(index[0], index[1], plantCard);
                }
            }
            return new PlayerModel(money, deck, activeDeck, field);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String activeIndexToCode(int index) {
        return String.valueOf((char) ('A' + index));
    }

    private int activeCodeToIndex(String code) {
        return code.charAt(0) - 'A';
    }

    private String fieldIndexToCode(int[] index) {
        return String.valueOf((char) ('A' + index[0])) + (index[1] + 1);
    }

    private int[] fieldCodeToIndex(String code) {
        return new int[]{code.charAt(0) - 'A', Character.getNumericValue(code.charAt(1)) - 1};
    }

    // Inner classes for XML serialization/deserialization
    private static class GameState {
        private int currentTurn;
        private List<ProductEntry> products;

        public int getCurrentTurn() {
            return currentTurn;
        }

        public void setCurrentTurn(int currentTurn) {
            this.currentTurn = currentTurn;
        }

        public List<ProductEntry> getProducts() {
            return products;
        }

        public void setProducts(List<ProductEntry> products) {
            this.products = products;
        }
    }

    private static class ProductEntry {
        private String name;
        private int count;

        public ProductEntry() {}

        public ProductEntry(String name, int count) {
            this.name = name;
            this.count = count;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }

    private static class PlayerData {
        private int money;
        private int deckSize;
        private int activeDeckSize;
        private List<ActiveCardData> activeDeck;
        private int fieldSize;
        private List<FieldCardData> field;

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }

        public int getDeckSize() {
            return deckSize;
        }

        public void setDeckSize(int deckSize) {
            this.deckSize = deckSize;
        }

        public int getActiveDeckSize() {
            return activeDeckSize;
        }

        public void setActiveDeckSize(int activeDeckSize) {
            this.activeDeckSize = activeDeckSize;
        }

        public List<ActiveCardData> getActiveDeck() {
            return activeDeck;
        }

        public void setActiveDeck(List<ActiveCardData> activeDeck) {
            this.activeDeck = activeDeck;
        }

        public int getFieldSize() {
            return fieldSize;
        }

        public void setFieldSize(int fieldSize) {
            this.fieldSize = fieldSize;
        }

        public List<FieldCardData> getField() {
            return field;
        }

        public void setField(List<FieldCardData> field) {
            this.field = field;
        }
    }

    private static class ActiveCardData {
        private String code;
        private String name;

        public ActiveCardData() {}

        public ActiveCardData(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    private static class FieldCardData {
        private String code;
        private String name;
        private int currentWeight;
        private int currentAge;
        private List<String> activeItems;

        public FieldCardData() {}

        public FieldCardData(String code, String name) {
            this.code = code;
            this.name = name;
            this.activeItems = new ArrayList<>();
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCurrentWeight() {
            return currentWeight;
        }

        public void setCurrentWeight(int currentWeight) {
            this.currentWeight = currentWeight;
        }

        public int getCurrentAge() {
            return currentAge;
        }

        public void setCurrentAge(int currentAge) {
            this.currentAge = currentAge;
        }

        public List<String> getActiveItems() {
            return activeItems;
        }

        public void setActiveItems(List<String> activeItems) {
            this.activeItems = activeItems;
        }
    }
}
