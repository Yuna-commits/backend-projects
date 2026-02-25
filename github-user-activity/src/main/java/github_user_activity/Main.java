package github_user_activity;

import github_user_activity.cli.GitHubCLI;
import github_user_activity.network.GitHubApiClient;
import github_user_activity.service.ActivityService;

public class Main {
    public static void main(String[] args) {
        // ApiClient -> Service -> CLI 순서로 의존성 주입
        new GitHubCLI(new ActivityService(new GitHubApiClient())).run(args);
    }
}
