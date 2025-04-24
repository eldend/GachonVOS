import { Link, NavLink } from 'react-router-dom';
import { MdKeyboardDoubleArrowLeft } from "react-icons/md";
import { MdKeyboardArrowLeft } from "react-icons/md";
import { MdKeyboardArrowRight } from "react-icons/md";
import { MdKeyboardDoubleArrowRight } from "react-icons/md";
import search from "../img/search.png";
import React, {useState,useEffect} from 'react';
import chat from '../img/chat.png';
import no from "../img/no.png";
import bubble from "../img/bubble.png";
import AdminHeader from "../main/AdminHeader";
import Footer from '../main/Footer';
import axios from "axios";
import { useNavigate } from 'react-router-dom';
import '../mypage/adminMypage.css';
const PostManage = () => {
    const [posts, setPosts] = useState([]);
    const [filteredPosts, setFilteredPosts] = useState([]);
    const [currentPage, setCurrentPage] = useState(1);
    const [postsPerPage] = useState(9);
    const [selectedCategory, setSelectedCategory] = useState('전체');
    const [selectedProcessState, setSelectedProcessState] = useState('전체');
    const [searchOption, setSearchOption] = useState('전체');
    const [searchField, setSearchField] = useState('전체');
    const [searchKeyword, setSearchKeyword] = useState('');
    const [isMounted, setIsMounted] = useState(true);
    const navigate = useNavigate();
    const activeStyle = {
        color: '#004E96',
        fontWeight: 700,
        borderBottom: '5px solid #004E96'
    };

    const handleEdit = (postId) => {

        navigate(`/edit/${postId}`);
        console.log(`Edit post with id ${postId}`);
    };

    const handleDelete = async (postId) => {
        try {
            const response = await axios.delete(`http://localhost:8080/api/post/${postId}`);

            if (response.status === 200) {
                console.log(`Post with id ${postId} deleted successfully`);
                window.location.reload("/postmanage")
            } else {
                console.error(`Failed to delete post with id ${postId}`);
            }
        } catch (error) {
            console.error("An error occurred while deleting the post", error);
        }
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

    const handleSearchOptionChange = (e) => {
        setSearchOption(e.target.value);
    
        // 선택된 처리상태 및 카테고리에 따라 필터링된 게시물을 설정
        filterPostsByStateAndCategory(selectedProcessState, e.target.value);
        
        // 첫 페이지로 이동
        setCurrentPage(1);
      };
    const handleProcessStateClick = (processState) => {
    setSelectedProcessState(processState);

    // process_state에 따라 필터링된 게시물을 설정
    if (processState === '전체') {
        setFilteredPosts(posts);
    } else {
        const filtered = posts.filter(post => post.processstate === processState);
        setFilteredPosts(filtered);
    }

    // 첫 페이지로 이동
    setCurrentPage(1);
    };
    const getTargetField = (post) => {
        const searchOptionToUse = searchOption !== '전체' ? searchOption : selectedCategory;

        if (searchField === 'title' && post.title.toLowerCase().includes(searchKeyword.toLowerCase())) {
            return post.title.toLowerCase();
        } else if (searchField === 'content' && post.content.toLowerCase().includes(searchKeyword.toLowerCase())) {
            return post.content.toLowerCase();
        } else if (searchField === 'hakbun' && post.hakbun && post.hakbun.toString().toLowerCase().includes(searchKeyword.toLowerCase())) {
            return post.hakbun.toString().toLowerCase();
        } else if (searchField === 'processstate' && post.processstate.toLowerCase().includes(selectedProcessState.toLowerCase())) {
            return post.processstate.toLowerCase();
        } else if (searchField === 'postcategory' && post.postcategory.toLowerCase().includes(searchOptionToUse.toLowerCase())) {
            return post.postcategory.toLowerCase();
        } else {
            return '';
        }
    };

    const handleSearch = () => {
        const keyword = searchKeyword.trim().toLowerCase();
    
        // 드롭박스 초기화
        setSelectedProcessState('전체');
        setSearchOption('전체');
    
        if (!keyword) {
            // 키워드가 없으면 모든 게시물 표시
            filterPostsByStateAndCategory('전체', '전체');
        } else {
            const filtered = posts.filter(post => {
                // 검색어, 카테고리 및 처리상태를 모두 확인하여 필터링
                const targetField = getTargetField(post);
                return (
                    (searchOption === '전체' || post.postcategory === searchOption) &&
                    targetField.includes(keyword) &&
                    (selectedProcessState === '전체' || post.processstate === selectedProcessState)
                );
            });
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

    const handleOpenNewTab = (url) => {
        const newWindow = window.open(url, "_blank", "noopener, noreferrer, width=900, height=500");

        if (newWindow) {
            newWindow.resizeTo(900, 500);
        }
    };
    const currentPosts = filteredPosts.slice(
        (currentPage - 1) * postsPerPage,
        currentPage * postsPerPage
    );
    // const handleCategoryClick = (category) => {
    //     setSelectedCategory(category);
    //
    //     // 카테고리에 따라 필터링된 게시물을 설정
    //     if (category === '전체') {
    //         setFilteredPosts(posts);
    //     } else {
    //         const filtered = posts.filter(post => post.postcategory === category);
    //         setFilteredPosts(filtered);
    //     }
    //
    //     // 첫 페이지로 이동
    //     setCurrentPage(1);
    // };
    const filterPostsByStateAndCategory = (processState, category) => {
        if (processState === '전체' && category === '전체') {
          // 모든 상태와 카테고리에 대한 필터링이 필요 없는 경우
          setFilteredPosts(posts);
        } else if (processState === '전체') {
          // 카테고리에 따라 필터링
          const filtered = posts.filter(post => post.postcategory === category);
          setFilteredPosts(filtered);
        } else if (category === '전체') {
          // 처리상태에 따라 필터링
          const filtered = posts.filter(post => post.processstate === processState);
          setFilteredPosts(filtered);
        } else {
          // 처리상태 및 카테고리 모두에 따라 필터링
          const filtered = posts.filter(post => post.processstate === processState && post.postcategory === category);
          setFilteredPosts(filtered);
        }
      };
    
    return (
        <div><AdminHeader/>
            <div class="board_wrap2">
                <div className='mypageWrap'>
                    <div className="menu-bar2">
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
                    <div className="search2">
                        <div className="custom-select">
                            <select value={selectedProcessState} onChange={(e) => handleProcessStateClick(e.target.value)}>
                                <option value="전체">전체</option>
                                <option value="처리대기">처리대기</option>
                                <option value="처리중">처리중</option>
                                <option value="처리완료">처리완료</option>
                            </select>
                            <select value={searchOption} onChange={(e) => handleSearchOptionChange(e)}>
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
                    <div class="board_list_wrap">
                        <div class="board_list2">
                            <div class="top">
                                <div class="num">번호</div>
                                <div class="title">제목</div>
                                <div class="writer">학번</div>
                                <div class="date">작성일</div>
                                <div class="manager">담당부서</div>
                                <div class="state">처리상태</div>
                                <div class="change">수정</div>
                                <div class="delete">삭제</div>
                            </div>
                            {currentPosts.map((post, index) => (
                                <div key={post.id}>
                                    <a href={`/admininside/${post.id}`}>
                                        <div className="num">{filteredPosts.length - index}</div>
                                        <div className="title">{post.title}</div>
                                        <div className="writer">{post.hakbun}</div>
                                        <div className="date">{post.createdDate}</div>
                                        <div className="manager">{post.postcategory}</div>
                                        <div className="state">{post.processstate}</div>
                                    </a>
                                    <button className="change" onClick={() => handleEdit(post.id)}>수정</button>
                                    <button className="delete" onClick={() => handleDelete(post.id)}>삭제</button>
                                </div>
                            ))}

                        </div>
                        <div className='bottomMuhanWrap'>
                        <div className="App1">
                            <img src={no} className='nothing' width="70px" height="91px"/>
                        </div>
                        <div className="board_page">
                            <a href="#" className="bt first" onClick={handleFirstPage}><MdKeyboardDoubleArrowLeft /></a>
                            {renderPageNumbers}
                            <a href="#" className="bt last" onClick={handleLastPage}><MdKeyboardDoubleArrowRight /></a>
                        </div>
                        <div className="App2">
                            <img src={chat} className='muhan' onClick={() => handleOpenNewTab("http://localhost:3000/chatbot")}
                                width="70px" height="91px"/>
                            <img src={bubble} className='bubble' onClick={() => handleOpenNewTab("http://localhost:3000/chatbot")} 
                                width="70px" height="40px"/>
                        </div>
                    </div>
                    </div>
                </div>
            </div><Footer/></div>
    );
};
export default PostManage;