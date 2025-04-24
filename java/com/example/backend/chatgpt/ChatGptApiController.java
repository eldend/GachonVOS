package com.example.backend.chatgpt;

import com.example.backend.application.dao.PostsRepository;
import com.example.backend.domain.PostCategory;
import com.example.backend.domain.ProcessState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/chat-gpt")
public class ChatGptApiController {

    private final ChatGptService chatGptService;
    private final PostsRepository postsRepository;
    private static final Logger logger = LoggerFactory.getLogger(ChatGptController.class);

    public ChatGptApiController(ChatGptService chatGptService, PostsRepository postsRepository) {
        this.chatGptService = chatGptService;
        this.postsRepository = postsRepository;
    }

    //1. 일반 챗봇 대화 api
    @PostMapping("/question")
    public ResponseEntity<ChatGptResponseDto> sendQuestion(@RequestBody QuestionRequestDto requestDto) {
        try {
            ChatGptResponseDto response = chatGptService.askQuestion(requestDto);
            System.out.println("question 요청 성공");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            // 에러 발생 시 예외 처리
            System.err.println("Error occurred while processing the question request: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //2. 필터링 api !!!
    @PostMapping("/filtering")
    public ResponseEntity<ChatGptResponseDto> sendFiltering(@RequestBody QuestionRequestDto requestDto) {
        try {
            logger.info("사용자가 입력한 질문: {}", requestDto.getQuestion());

            ChatGptResponseDto response = chatGptService.askFiltering(requestDto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("질문 처리 중 오류 발생: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //3. 브리핑 api
    @GetMapping("/briefing")
    public ResponseEntity<ChatGptResponseDto> sendBriefing() {
        try {
            ChatGptResponseDto originalResponse = chatGptService.getBriefingResponse();
            //처리 대기상태인 데이터의 개수를 가져오기 위한 쿼리
            int countOfUnprocessedPosts = postsRepository.countByProcessState(ProcessState.처리대기);
            // 카테고리별 개수를 가져오는 쿼리를 통해 해당 카테고리의 개수를 가져옴
            int countOfAcademicPosts = postsRepository.countByPostCategoryAndProcessState(PostCategory.학사, ProcessState.처리대기);
            int countOfFacilityPosts = postsRepository.countByPostCategoryAndProcessState(PostCategory.시설, ProcessState.처리대기);
            int countOfSchoolLifePosts = postsRepository.countByPostCategoryAndProcessState(PostCategory.학교생활, ProcessState.처리대기);
            int countOfProposalPosts = postsRepository.countByPostCategoryAndProcessState(PostCategory.정책제안, ProcessState.처리대기);
            int countOfOtherPosts = postsRepository.countByPostCategoryAndProcessState(PostCategory.기타, ProcessState.처리대기);

            String modifiedText = "현재 처리 대기중인 건의글에 대해 브리핑하겠습니다! 미처리된 건의글은 총 " +
                    countOfUnprocessedPosts + "건 있습니다. " +
                    "각 카테고리별로는 학사 " + countOfAcademicPosts + "건, " +
                    "시설 " + countOfFacilityPosts + "건, " +
                    "학교생활 " + countOfSchoolLifePosts + "건, " +
                    "정책제안 " + countOfProposalPosts + "건, " +
                    "기타 " + countOfOtherPosts + "건이 있습니다. " +
                    "미처리된 건의글에 대해 간략히 설명하자면, " + originalResponse.getText();

            ChatGptResponseDto modifiedResponse = new ChatGptResponseDto();
            modifiedResponse.setText(modifiedText);

            return new ResponseEntity<>(modifiedResponse, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error occurred while processing the briefing request: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}