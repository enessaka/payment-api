//package servicesimpl;
//
//import io.restassured.specification.RequestSpecification;
//import net.thucydides.core.annotations.Step;
//
//import static com.payee.test.utils.restassured.Requests.*;
//import static com.payee.test.utils.restassured.RestAssuredFactory.getRestClient;
//
//public class TrelloApi {
//    private final RequestSpecification reqSpec = getRestClient();
//    private static final String BOARDS_ENDPOINT = "boards/";
//    private static final String ALL_BOARDS_ENDPOINT = "members/me/" + BOARDS_ENDPOINT;
//
//    @Step("Get all boards")
//    public void getAllBoards() {
//        get(reqSpec, ALL_BOARDS_ENDPOINT);
//    }
//
//    @Step("Get board with id {0}")
//    public void getBoard(String id) {
//        get(reqSpec, String.format("%s%s", BOARDS_ENDPOINT, id));
//    }
//
//    @Step("Delete board with id {0}")
//    public void deleteBoard(String id) {
//        delete(reqSpec, String.format("%s%s", BOARDS_ENDPOINT, id));
//    }
//
//    @Step("Create a new board")
//    public void createBoard(CreateBoardRequest payload) {
//        post(reqSpec, BOARDS_ENDPOINT, payload);
//    }
//
//    @Step("Update {1} board name")
//    public void updateBoard(CreateBoardRequest payload, String id) {
//        put(reqSpec, String.format("%s%s", BOARDS_ENDPOINT, id), payload);
//    }
//
//    @Step("Generate {0} board creation payload")
//    public CreateBoardRequest generateCreateBoardPayload(String name) {
//        return new CreateBoardRequest().name(name);
//    }
//}
