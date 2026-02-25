package github_user_activity.network;

import github_user_activity.exception.ApiException;
import github_user_activity.exception.UserNotFoundException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GitHubApiClient {

    private static final String GITHUB_API_URL = "https://api.github.com/users/%s/events";

    /**
     * HTTP GET 요청을 보내 특정 사용자의 최근 활동 JSON 데이터 조회
     *
     * @param username 조회할 대상의 GitHub 사용자 이름
     * @return API로부터 응답받은 순서 JSON 문자열
     * @throws Exception 상태 코드에 따른 커스텀 예외
     */
    public String fetchUserActivity(String username) throws Exception {
        // 1. HttpClient 인스턴스 생성 및 요청 조립
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request
                = HttpRequest.newBuilder().uri(URI.create(String.format(GITHUB_API_URL, username))).GET().build();

        // 2. 동기 방식으로 요청 전송 및 응답 수신
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int statusCode = response.statusCode();

        // 3. HTTP 상태 코드에 따른 분기 처리
        return switch (statusCode) {
            case 200 -> response.body();
            case 404 -> throw new UserNotFoundException("User not found. Please check the GitHub username.");
            case 403 -> throw new ApiException("API rate limit exceeded. Please try again later.");
            case 500, 502, 503 ->
                    throw new ApiException("GitHub server is currently experiencing issues. (Code: " + statusCode + ")");
            default -> throw new ApiException("An unexpected error occurred. (Code:" + statusCode + ")");
        };
    }

}
