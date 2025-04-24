package com.example.backend.chatgpt;

import com.example.backend.application.dao.PostsRepository;
import com.example.backend.domain.ProcessState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("view/chat-gpt")
public class ChatGptController {

    private final ChatGptService chatGptService;
    private final PostsRepository postsRepository; // PostsRepository 주입을 위한 필드 추가
    private static final Logger logger = LoggerFactory.getLogger(ChatGptController.class);

    public ChatGptController(ChatGptService chatGptService, PostsRepository postsRepository) {
        this.chatGptService = chatGptService;
        this.postsRepository = postsRepository;
    }

    //기존 chatgpt 사용 api
    @PostMapping("/question")
    public ChatGptResponseDto sendQuestion(@RequestBody QuestionRequestDto requestDto) {
        return chatGptService.askQuestion(requestDto);
    }

    //필터링 api !!!
    @PostMapping("/filtering")
    public ModelAndView sendQuestion(@RequestParam("question") String question) {
        // 사용자가 입력한 질문 콘솔에 로깅
        logger.info("사용자가 입력한 질문: {}", question);

        // 질문을 QuestionRequestDto 객체로 만들어서 서비스로 전달
        QuestionRequestDto requestDto = new QuestionRequestDto();
        requestDto.setQuestion(question);

        // ChatGptService를 통해 ChatGPT에게 질문 보내고 응답 받기
        ChatGptResponseDto response = chatGptService.askFiltering(requestDto);

        // ModelAndView를 생성하고 chatResponse라는 이름으로 응답을 뷰에 전달
        ModelAndView modelAndView = new ModelAndView("user_setting");
        modelAndView.addObject("chatResponse", response);

        return modelAndView;
    }


    //문자열 앞에 붙여서 출력하는 브리핑 api
    @GetMapping("/briefing")
    public ModelAndView getBriefingResponse() {

        // "처리대기" 상태의 데이터 개수를 DB에서 조회
        int countOfUnprocessedPosts = postsRepository.countByProcessState(ProcessState.처리대기);

        // 기존의 ChatGptResponseDto를 가져오는 코드 (chatGptService.getBriefingResponse()를 사용하는 것으로 가정합니다)
        ChatGptResponseDto originalResponse = chatGptService.getBriefingResponse();

        // 받아온 개수를 특정 문구에 포함하여 응답 문자열을 만듦
        String modifiedText = "처리 대기중인 건의글에 대해 브리핑하겠습니다. 현재 미처리된 건의글은 " +
                countOfUnprocessedPosts + "건 있습니다. " +
                "미처리된 건의글에 대해 간략히 설명하자면, " + originalResponse.getText();

        // 기존의 ChatGptResponseDto를 활용하여 새로운 응답 객체 생성
        ChatGptResponseDto modifiedResponse = new ChatGptResponseDto();
        modifiedResponse.setText(modifiedText);

        ModelAndView modelAndView = new ModelAndView("user_setting");
        modelAndView.addObject("briefResponse", modifiedResponse);

        return modelAndView;
    }

}