package github_user_activity.cli;

import github_user_activity.exception.ApiException;
import github_user_activity.exception.UserNotFoundException;
import github_user_activity.model.GitHubEvent;
import github_user_activity.service.ActivityService;

import java.util.List;

public class GitHubCLI {

    private final ActivityService activityService;

    public GitHubCLI(ActivityService activityService) {
        this.activityService = activityService;
    }

    /**
     * CLI 프로그램 실행 및 흐름 제어
     *
     * @param args 프로그램 실행 시 터미널로부터 전달받은 인자 배열
     */
    public void run(String[] args) {
        // 1. 입력값 검증: 인자가 없을 경우 프로그램 사용법 안내
        if (args.length == 0) {
            System.out.println("Usage: github-activity <username>");
            return;
        }

        // 2. 공백 제거, 유효성 검증
        String username = args[0].trim(); // github 사용자 이름

        if (username.isEmpty()) {
            System.out.println("Error: Username cannot be empty.");
            return;
        }

        try {
            // 3. 비즈니스 로직 호출: username을 통해 사용자 활동 내역 조회
            List<GitHubEvent> events = activityService.getUserActivity(username);

            // 활동 내역이 없는 경우
            if (events.isEmpty()) {
                System.out.println("No recent activity found.");
                return;
            }

            // 4. 결과 출력
            System.out.println("Output:");
            List<String> formattedMessages = activityService.formatEvents(events);
            for (String message : formattedMessages) {
                System.out.println(message);
            }

        } catch (UserNotFoundException | ApiException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred.");
            System.out.println("Details: " + e.getMessage());
        }
    }

}
