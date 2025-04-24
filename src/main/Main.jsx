import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { MdKeyboardDoubleArrowLeft, MdKeyboardDoubleArrowRight } from "react-icons/md";
import search from "../img/search.png";
import Header from "./Header";
import Footer from "./Footer";

const Main = () => {
  const [posts, setPosts] = useState([]);
  const [filteredPosts, setFilteredPosts] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [postsPerPage] = useState(9);
  const [selectedCategory, setSelectedCategory] = useState('전체');
  const [searchOption, setSearchOption] = useState('전체');
  const [searchField, setSearchField] = useState('전체');
  const [searchKeyword, setSearchKeyword] = useState('');
  const [isMounted, setIsMounted] = useState(true);
  const activeStyle = {
    color: '#004E96',
    fontWeight: 700,
    borderBottom: '5px solid #004E96'
  };

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axios.get('http://localhost:8080/api/post');
        const sortedPosts = response.data.sort((a, b) => b.id - a.id);

        const updatedPosts = await Promise.all(
            sortedPosts.map(async (post) => {
              const hakbun = await fetchUserInfo(post.userId);
              return { ...post, hakbun };
            })
        );

        if (isMounted) {
          setPosts(updatedPosts);
          setFilteredPosts(updatedPosts);
        }
      } catch (error) {
        console.error('게시물 데이터를 불러오는 중 오류 발생:', error);
      }
    };

    fetchData();

    return () => {
      setIsMounted(false);
    };
  }, []);

  useEffect(() => {
    fetchDataForUser();
  }, [posts, currentPage, isMounted, selectedCategory, searchOption, searchKeyword]);

  const fetchDataForUser = async () => {
    try {
      const updatedPosts = await Promise.all(
          posts.map(async (post) => {
            const hakbun = await fetchUserInfo(post.userId);
            return { ...post, hakbun };
          })
      );

      if (isMounted) {
        setFilteredPosts(updatedPosts);
      }
    } catch (error) {
      console.error('유저 데이터를 불러오는 중 오류 발생:', error);
    }
  };

  const handleCategoryClick = (category) => {
    setSelectedCategory(category);

    // 카테고리에 따라 필터링된 게시물을 설정
    if (category === '전체') {
      setFilteredPosts(posts);
    } else {
      const filtered = posts.filter(post => post.postcategory === category);
      setFilteredPosts(filtered);
    }

    // 첫 페이지로 이동
    setCurrentPage(1);
  };

  const handleFirstPage = () => {
    setCurrentPage(1);
  };

  const handleLastPage = () => {
    setCurrentPage(Math.ceil(filteredPosts.length / postsPerPage));
  };

  const paginate = pageNumber => {
    setCurrentPage(pageNumber);
  };

  // 검색 옵션 변경 이벤트 핸들러 수정
  const handleSearchOptionChange = (e) => {
    setSearchOption(e.target.value);
    const categoryToFilter = e.target.value === '전체' ? selectedCategory : e.target.value;

    // 선택된 카테고리에 따라 필터링된 게시물을 설정
    if (categoryToFilter === '전체') {
      setFilteredPosts(posts);
    } else {
      const filtered = posts.filter(post => post.postcategory === categoryToFilter);
      setFilteredPosts(filtered);
    }

    // 첫 페이지로 이동
    setCurrentPage(1);
  };

// 학번 검색을 위한 수정
  const getTargetField = (post) => {
    switch (searchField) {
      case 'title':
        return post.title.toLowerCase();
      case 'content':
        return post.content.toLowerCase();
      case 'hakbun':
        // 수정: hakbun이 숫자인 경우도 고려
        return post.hakbun ? post.hakbun.toString().toLowerCase() : '';
      default:
        return '';
    }
  };

// 학번 검색 버튼 클릭 핸들러 수정
  const handleSearch = () => {
    const keyword = searchKeyword.trim().toLowerCase();

    if (!keyword) {
      // 키워드가 없으면 모든 게시물 표시
      setFilteredPosts(posts);
    } else {
      const filtered = posts.filter(post => {
        // 카테고리 및 필드를 모두 확인하여 필터링
        const targetField = getTargetField(post);
        return (
            (searchOption === '전체' || post.postcategory === searchOption) &&
            targetField.includes(keyword)
        );
      });
      console.log('Filtered Posts:', filtered);
      setFilteredPosts(filtered);
    }
    setCurrentPage(1);
  };




  const renderPageNumbers = Array.from({ length: Math.ceil(filteredPosts.length / postsPerPage) }, (_, index) => (
      <a
          key={index + 1}
          href="#"
          className={`num ${currentPage === index + 1 ? 'on' : ''}`}
          style={currentPage === index + 1 ? activeStyle : {}}
          onClick={() => paginate(index + 1)}
      >
        {index + 1}
      </a>
  ));

  const fetchUserInfo = async (userId) => {
    const userApiUrl = `http://localhost:8080/api/user/${userId}`;
    try {
      const response = await axios.get(userApiUrl, { withCredentials: true });
      return response.data.hakbun;
    } catch (error) {
      console.error('유저 데이터를 불러오는 중 오류 발생:', error);
      return '';
    }
  };
  const currentPosts = filteredPosts.slice(
      (currentPage - 1) * postsPerPage,
      currentPage * postsPerPage
  );
  return (
      <div><Header/>
      <div className="board_wrap">
        <div className="menu-bar">
          <nav>
            <ul>
              <li onClick={() => handleCategoryClick('전체')}>전체</li>
              <li onClick={() => handleCategoryClick('학사')}>학사</li>
              <li onClick={() => handleCategoryClick('시설')}>시설</li>
              <li onClick={() => handleCategoryClick('학교생활')}>학교생활</li>
              <li onClick={() => handleCategoryClick('정책제안')}>정책제안</li>
              <li onClick={() => handleCategoryClick('기타')}>기타</li>
            </ul>
          </nav>
        </div>
        <div className="search">
          <div className="custom-select">
            <select value={searchOption} onChange={handleSearchOptionChange}>
              <option value="전체">전체</option>
              <option value="학사">학사</option>
              <option value="시설">시설</option>
              <option value="학교생활">학교생활</option>
              <option value="정책제안">정책제안</option>
              <option value="기타">기타</option>
            </select>
            <select value={searchField} onChange={(e) => setSearchField(e.target.value)}>
              <option value="전체">전체</option>
              <option value="title">제목</option>
              <option value="content">내용</option>
              <option value="hakbun">학번</option>
            </select>
          </div>
          <form>
            <input
                type="text"
                className="form-control"
                value={searchKeyword}
                onChange={(e) => setSearchKeyword(e.target.value)}
            />
            <button type="button" className="searchBtn" onClick={handleSearch}>
              <img src={search} className="searchIcon" width="15px" height="15px" />
            </button>
          </form>
        </div>
        <div className="board_list">
          <div className="top">
            <div className="num">번호</div>
            <div className="title">제목</div>
            <div className="writer">학번</div>
            <div className="date">작성일</div>
            <div className="manager">카테고리</div>
            <div className="state">처리상태</div>
          </div>
          {currentPosts.map((post, index) => (
              <div key={post.id}>
                <a href={`/inside/${post.id}`}>
                  <div className="num">{filteredPosts.length - index}</div>
                  <div className="title">{post.title}</div>
                  <div className="writer">{post.hakbun}</div>
                  <div className="date">{post.createdDate}</div>
                  <div className="manager">{post.postcategory}</div>
                  <div className="state">{post.processstate}</div>
                </a>
              </div>
          ))}
        </div>
        <div className="bt_wrap">
          <a href="/write" className="on">건의글 작성</a>
        </div>
        <div className="board_page">
          <a href="#" className="bt first" onClick={handleFirstPage}><MdKeyboardDoubleArrowLeft /></a>
          {renderPageNumbers}
          <a href="#" className="bt last" onClick={handleLastPage}><MdKeyboardDoubleArrowRight /></a>
        </div>
      </div><Footer/></div>
  );
};

export default Main;