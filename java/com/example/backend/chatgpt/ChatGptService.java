package com.example.backend.chatgpt;

import com.example.backend.application.dao.PostsRepository;
import com.example.backend.domain.Posts;
import com.example.backend.domain.ProcessState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

//import java.net.http.HttpHeaders;

@Service
public class ChatGptService {

    private static final Logger logger = LoggerFactory.getLogger(ChatGptService.class);
    private static RestTemplate restTemplate = new RestTemplate();
    private final PostsRepository postsRepository;

    public ChatGptService(PostsRepository postsRepository) {
        this.postsRepository = postsRepository;
    }

    public HttpEntity<ChatGptRequestDto> buildHttpEntity(ChatGptRequestDto requestDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(ChatGptConfig.MEDIA_TYPE));
        headers.add(ChatGptConfig.AUTHORIZATION, ChatGptConfig.BEARER + ChatGptConfig.API_KEY);
        return new HttpEntity<>(requestDto, headers);
    }

    public ChatGptResponseDto getResponse(HttpEntity<ChatGptRequestDto> chatGptRequestDtoHttpEntity) {
        ResponseEntity<ChatGptResponseDto> responseEntity = restTemplate.postForEntity(
                ChatGptConfig.URL,
                chatGptRequestDtoHttpEntity,
                ChatGptResponseDto.class);

        ChatGptResponseDto response = responseEntity.getBody();
        logger.info("ChatGPT 응답: {}", response); // ChatGPT 응답 로깅

        if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
            ChatGptChoice choice = response.getChoices().get(0); // 첫 번째 choice만 고려
            if (choice != null && choice.getText() != null) {
                response.setText(choice.getText()); // 응답 텍스트를 Choice 안의 text로 설정
            }
        }
        return response;
    }

    //기존에 질문을 보내는 글
    public ChatGptResponseDto askQuestion(QuestionRequestDto requestDto) {
        return this.getResponse(
                this.buildHttpEntity(
                        new ChatGptRequestDto(
                                ChatGptConfig.MODEL,
                                requestDto.getQuestion(),
                                ChatGptConfig.MAX_TOKEN,
                                ChatGptConfig.TEMPERATURE,
                                ChatGptConfig.TOP_P
                        )
                )
        );
    }


    //필터링요청을 보내는 코드
    public ChatGptResponseDto askFiltering(QuestionRequestDto requestDto) {
        String questionWithSummary = "다음 글을 건의문 형식에 알맞게 정중하고 공적인 글로 수정해주세요. " + requestDto.getQuestion();
        requestDto.setQuestion(questionWithSummary); // 혹은 해당 필드에 직접 설정

        // 챗지피티에게 요청을 보내고 응답을 받아옴
        ChatGptResponseDto chatGptResponse = this.getResponse(
                this.buildHttpEntity(
                        new ChatGptRequestDto(
                                ChatGptConfig.MODEL,
                                requestDto.getQuestion(),
                                ChatGptConfig.MAX_TOKEN,
                                ChatGptConfig.TEMPERATURE,
                                ChatGptConfig.TOP_P
                        )
                )
        );
        return chatGptResponse;
    }

    //브리핑 요청을 보내는 코드
    public ChatGptResponseDto getBriefingResponse() {
        //process_state가 "처리대기"인 데이터들의 title값을 가져오기
        List<Posts> posts = postsRepository.findByProcessState(ProcessState.처리대기);
        List<String> titles = getTitlesFromPosts();

        String concatenatedTitles = String.join(". ", titles); // titles를 문자열로 나열하여 구분자로 구분
        logger.info("concatenatedTitles 값: {}", concatenatedTitles);        // 로그 출력

        String questionWithSummary = "다음 글들을 읽고, 어떤 건의 사항들이 있는지 간단히 요약해서 말해주고 전반적인 여론이 어떤지 설명해주세요. " + concatenatedTitles;

        // ChatGPT에게 요청을 보내고 응답을 받아옴
        ChatGptResponseDto chatGptResponse = this.getResponse(
                this.buildHttpEntity(
                        new ChatGptRequestDto(
                                ChatGptConfig.MODEL,
                                questionWithSummary,
                                ChatGptConfig.MAX_TOKEN,
                                ChatGptConfig.TEMPERATURE,
                                ChatGptConfig.TOP_P
                        )
                )
        );
        return chatGptResponse;
    }

    //id=1,2,3,4,5인 title 찾아올때 임시적으로 쓴거.. 혹시 쓸일 있을지 모르니까 주석 남겨놓음!
//    private List<String> getTitlesFromPosts(List<Long> postIds) {
//        // DB에서 postIds에 해당하는 레코드의 title을 가져와 리스트로 반환하는 로직 작성
//        List<Posts> posts = postsRepository.findAllByIdIn(postIds);
//        return posts.stream().map(Posts::getTitle).collect(Collectors.toList());
//    }

    private List<String> getTitlesFromPosts() {
        // DB에서 process_state가 '처리대기'인 레코드의 title을 가져와 리스트로 반환하는 로직 작성
        List<Posts> posts = postsRepository.findByProcessState(ProcessState.처리대기);
        return posts.stream().map(Posts::getTitle).collect(Collectors.toList());
    }

}