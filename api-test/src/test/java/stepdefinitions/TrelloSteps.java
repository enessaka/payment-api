//package stepdefinitions;
//
//import com.trello.qa.models.CreateBoardRequest;
//import com.trello.qa.models.getboard.GetBoardResponse;
//import cucumber.api.java.en.Given;
//import cucumber.api.java.en.Then;
//import cucumber.api.java.en.When;
//import net.serenitybdd.core.Serenity;
//import net.thucydides.core.annotations.Steps;
//import org.apache.http.HttpStatus;
//import org.assertj.core.api.Assertions;
//import org.assertj.core.api.SoftAssertions;
//import servicesimpl.ResponseAssertion;
//import servicesimpl.TrelloApi;
//
//import java.sql.Timestamp;
//import java.util.Arrays;
//
//import static com.trello.qa.utils.restassured.Requests.then;
//
//public class TrelloSteps {
//    private static final String BOARD_ID = "boardId";
//    private static final String BOARD_NAME = "boardName";
//
//    private static final SoftAssertions softAssert = new SoftAssertions();
//
//    @Steps
//    TrelloApi trelloApi;
//
//    @Steps
//    ResponseAssertion responseAssertion;
//
//    @Given("board is created")
//    public void ensureBoardIsCreated() {
//        if (!Serenity.hasASessionVariableCalled(BOARD_ID)) {
//            String boardName = generateUniqueBoardName();
//            createANewBoard(boardName);
//            validateSuccessBoardCreation();
//        }
//    }
//
//    @When("I retrieve all boards")
//    public void retrieveAllBoards() {
//        trelloApi.getAllBoards();
//    }
//
//    @When("I retrieve the board")
//    public void retrieveASingleBoard() {
//        String id = Serenity.sessionVariableCalled(BOARD_ID);
//        trelloApi.getBoard(id);
//    }
//
//    @When("I delete the board")
//    public void deleteBoard() {
//        String id = Serenity.sessionVariableCalled(BOARD_ID);
//        trelloApi.deleteBoard(id);
//    }
//
//    @When("I try to create a board with empty name")
//    public void tryToCreateBoardEmptyName() {
//        CreateBoardRequest emptyName = trelloApi.generateCreateBoardPayload(null);
//        trelloApi.createBoard(emptyName);
//    }
//
//    @When("I create a new board with {string} name")
//    public void createANewBoard(String name) {
//        CreateBoardRequest createBoardRequest = trelloApi.generateCreateBoardPayload(name);
//        trelloApi.createBoard(createBoardRequest);
//        Serenity.setSessionVariable(BOARD_NAME).to(name);
//    }
//
//    @When("I update an existing board name")
//    public void createANewBoard() {
//        String boardName = generateUniqueBoardName();
//        CreateBoardRequest updateBoard = trelloApi.generateCreateBoardPayload(boardName);
//        trelloApi.updateBoard(updateBoard, Serenity.sessionVariableCalled(BOARD_ID));
//        Serenity.setSessionVariable(BOARD_NAME).to(boardName);
//    }
//
//    @Then("the board should be successfully created")
//    public void validateSuccessBoardCreation() {
//        responseAssertion.assertStatusCode(HttpStatus.SC_OK);
//        GetBoardResponse board = then().extract().as(GetBoardResponse.class);
//
//        Assertions.assertThat(board.getId()).as("board id is null").isNotNull();
//        Assertions.assertThat(board.getName()).as("Board name is not correct")
//                .isEqualTo(Serenity.sessionVariableCalled(BOARD_NAME));
//        Serenity.setSessionVariable(BOARD_ID).to(board.getId());
//    }
//
//    @Then("I should receive the (updated )board details")
//    public void validateSingleBoard() {
//        responseAssertion.assertStatusCode(HttpStatus.SC_OK);
//        GetBoardResponse board = then().extract().as(GetBoardResponse.class);
//
//        Assertions.assertThat(board).as("There is no Board").isNotNull();
//        softAssert.assertThat(board.getId()).as("board id is not correct")
//                .isEqualTo(Serenity.sessionVariableCalled(BOARD_ID));
//        softAssert.assertThat(board.getName()).as("board name is not correct")
//                .isEqualTo(Serenity.sessionVariableCalled(BOARD_NAME));
//
//        softAssert.assertAll();
//    }
//
//    @Then("I should receive all boards")
//    public void validateAllBoards() {
//        responseAssertion.assertStatusCode(HttpStatus.SC_OK);
//        GetBoardResponse[] boardResponse = then().extract().as(GetBoardResponse[].class);
//
//        Assertions.assertThat(boardResponse).as("There is no Board").isNotEmpty();
//        Arrays.stream(boardResponse).forEach(board -> {
//            softAssert.assertThat(board.getId()).as("board id is null").isNotNull();
//            softAssert.assertThat(board.getShortUrl()).as("board short url is null").isNotNull();
//            softAssert.assertThat(board.getName()).as("board name is null").isNotNull();
//        });
//
//        softAssert.assertAll();
//    }
//
//    private String generateUniqueBoardName() {
//        return String.format("TXN-%s", (new Timestamp(System.currentTimeMillis())).getTime());
//    }
//}
