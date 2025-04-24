import React, { useState } from 'react';
import { FaArrowRight } from 'react-icons/fa';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import './write.css';
import Header from '../main/Header';
import Footer from '../main/Footer';

const Write = () => {
  const [postCategory, setCategory] = useState('기타');
  const [title, setTitle] = useState('');
  const [question, setQuestion] = useState('');
  const [filteredContent, setFilteredContent] = useState('');

  const handleCategoryChange = (e) => {
    setCategory(e.target.value);
  };

  const handleFiltering = async () => {
    try {
      const response = await axios.post(`http://localhost:8080/api/chat-gpt/filtering`, {
        question,
      });

      if (!response.data || response.status !== 200) {
        throw new Error('필터링 서버 응답 오류');
      }

      setFilteredContent(response.data.text);
    } catch (error) {
      console.error('필터링 에러:', error);
    }
  };
  const navigate = useNavigate();

  const handlePostSubmission = async (e) => {
    e.preventDefault();

    try {
      const response = await axios.post(`http://localhost:8080/api/post`, {
            postCategory,
            title,
            content: filteredContent
          },
          { withCredentials: true }
      );

      if (!response.data || response.status !== 200) {
        throw new Error('건의하기 서버 응답 오류');
      }

      console.log(response.data.message); // 개발 중 디버깅용 로깅, 프로덕션에서는 적절한 처리로 변경 필요
      // 페이지 이동
      navigate('/vos'); // '/your-pathname'에 실제 경로를 입력하세요
    } catch (error) {
      console.error('건의하기 에러:', error);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      await handleFiltering();
      await handlePostSubmission();
    } catch (error) {
      console.error('에러 발생:', error);
    }
  };

  return (
      <div><Header/>
      <div className='WritePage'>
        <div className='Box'>
          <div className='introtitle'>이용방법</div>
          <div className='writeintro'>
            <div className='introContent'>
              - 학사: 수업, 학적, 졸업, 등록, 교수/조교인사관련 학사/교무행정 관련
            </div>
            <div className='introContent'>
              - 시설: 시설, 미화, 통학버스, 기자재 등 시설관리 관련
            </div>
            <div className='introContent'>
              - 학교생활: 장학, 학생자치, 편의서비스, 상담, 취·창업 등 학교생활 관련
            </div>
            <div className='introContent'>
              - 정책제안: 행복한 학교생활을 위한 정책제안 또는 제도개선 제안
            </div>
            <div className='introContent'>
              - 기타의견: 위 사항에 해당하지 않은 다양하고 창의적인 의견
            </div>
            <br></br>
            <div className='introContent'>
              - 제목에 건의 목적을 명확히 기재해야 하며 내용은 350자 이내로 작성해야 합니다.
            </div>
            <div className='introContent'>
              - 원활한 필터링 기능을 위해 문장부호(.?!등) 외의 기호, 이모지를 쓰지 말아야하며 줄바꿈은 되도록 하지 말아야 합니다.
            </div>
            <div className='introContent'>
              - 단순비방, 비인격적인 용어사용, 홍보 등이 글에 포함될 경우 무통보 임의삭제 될 수 있습니다.
            </div>
          </div>
        </div>
        <div className='write'>
          <form onSubmit={handleSubmit}>
            <div className='select'>
              <label htmlFor='category'>카테고리</label>
              <select id='category' value={postCategory} onChange={handleCategoryChange}>
                {/*<option value='전체'>전체</option>*/}
                <option value='학사'>학사</option>
                <option value='시설'>시설</option>
                <option value='학교생활'>학교생활</option>
                <option value='정책제안'>정책제안</option>
                <option value='기타'>기타</option>
              </select>
            </div>
            <div className='titleform'>
              <div className='form-group'>
                <label htmlFor='title'>제목</label>
                <input type='text' id='title' value={title} onChange={(e) => setTitle(e.target.value)} />
              </div>
            </div>
            <div className='titleform'>
              <div className='form-group2'>
              <textarea
                  maxLength='350'
                  className='form-control'
                  rows='5'
                  id='content'
                  name='content'
                  defaultValue={question}
                  onChange={(e) => setQuestion(e.target.value)}
              ></textarea>
              </div>
              <div className='icon2'>
                <FaArrowRight />
                <button type='submit' onClick={handleFiltering}>필터링</button>
              </div>
              <div className='form-group2'>
                <div maxLength='350' className='form-control' rows='5' id='filteredContent' name='filteredContent'>
                  {filteredContent}
                </div>
              </div>
            </div>
          </form>
        </div>
        <div className='writebutton'>
          <button type='submit' className='btn' onClick={handlePostSubmission}>
            건의하기
          </button>
        </div>
      </div><Footer/></div>
  );
};

export default Write;