import React, { useState, useEffect, useRef} from 'react';
import { MdKeyboardDoubleArrowLeft } from "react-icons/md";
import { MdKeyboardArrowLeft } from "react-icons/md";
import { MdKeyboardArrowRight } from "react-icons/md";
import { MdKeyboardDoubleArrowRight } from "react-icons/md";
import search from "../img/search.png";
import "./usermanage.css"
import chat from '../img/chat.png';
import testuser from "../img/testuser.png";
import close from "../img/close.png";
import no from "../img/no.png";
import bubble from "../img/bubble.png";
import AdminHeader from "../main/AdminHeader";
import Footer from '../main/Footer';

const UserManage = () => {
    const [userList, setUserList] = useState([]);
    const [modalOpen, setModalOpen] = useState(false);
    const [filteredUserList, setFilteredUserList] = useState([]); 
    const [currentPage, setCurrentPage] = useState(1);
    const [postsPerPage] = useState(9);
    const [searchKeyword, setSearchKeyword] = useState(''); // Add this line
    const [searchOption, setSearchOption] = useState('username'); // 초기 검색 옵션을 'username'로 설정
    const indexOfLastPost = currentPage * postsPerPage;
    const indexOfFirstPost = indexOfLastPost - postsPerPage;
    const currentPosts = userList.slice(indexOfFirstPost, indexOfLastPost);
    const [selectedUser, setSelectedUser] = useState(null);
    const activeStyle = {
        color: '#004E96',
        fontWeight: 700,
        borderBottom: '5px solid #004E96'
    };

    const modalBackground = useRef();

    const handleOpenNewTab = (url) => {
        const newWindow = window.open(url, "_blank", "noopener, noreferrer, width=900, height=500");

        if (newWindow) {
            newWindow.resizeTo(900, 500);
        }
    };
  
    useEffect(() => {
        fetchUserList(); // 페이지가 로드될 때마다 사용자 목록을 불러오도록 변경
    }, [searchOption]);
    
    const fetchUserList = async () => {
        try {
            const response = await fetch('/api/user');
            const data = await response.json();
            setUserList(data);
    
            // 검색 관련 상태에 따라 필터된 목록 업데이트
            const filtered = applySearchFilter(data, searchOption, searchKeyword);
            setFilteredUserList(filtered);
        } catch (error) {
            console.error('사용자 목록을 불러오는 중 오류 발생:', error);
        }
    };
    
    const applySearchFilter = (list, option, keyword) => {
        const lowerCaseKeyword = keyword.trim().toLowerCase();
    
        if (!lowerCaseKeyword) {
            // 검색 키워드가 없을 때는 전체 목록을 사용
            return list;
        }
    
        const optionToFilter = option === '전체' ? '전체' : option;
    
        return list.filter(user => {
            const targetFields = optionToFilter === '전체'
                ? Object.keys(user).filter(key => key !== 'id').map(key => String(user[key]).toLowerCase())
                : [String(user[optionToFilter]).toLowerCase()];
    
            return targetFields.some(field => field.startsWith(lowerCaseKeyword));
        });
    };
      const handleFirstPage = () => {
        setCurrentPage(1);
    };

    const handleLastPage = () => {
        setCurrentPage(Math.ceil(userList.length / postsPerPage));
    };

    const paginate = pageNumber => {
        setCurrentPage(pageNumber);
    };
    
    const renderPageNumbers = Array.from({ length: Math.ceil(userList.length / postsPerPage) }, (_, index) => (
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
    const handleSearchOptionChange = (e) => {
        const selectedOption = e.target.value;
        setSearchOption(selectedOption);
        setSearchKeyword('');
        setCurrentPage(1);
        setFilteredUserList(userList); // 검색 키워드를 비웠을 때 필터된 목록을 전체 유저 목록으로 설정
    };
    const handleSearch = (e) => {
        e.preventDefault(); // 기본 동작 막기
    
        const keyword = searchKeyword.trim().toLowerCase();
    
        if (!keyword) {
            // 검색 키워드가 없을 때는 전체 유저 리스트를 사용
            setFilteredUserList(userList);
        } else {
            const optionToFilter = searchOption === '전체' ? '전체' : searchOption;
    
            const filtered = userList.filter(user => {
                const targetFields = optionToFilter === '전체'
                    ? Object.keys(user).filter(key => key !== 'id').map(key => String(user[key]).toLowerCase())
                    : [String(user[optionToFilter]).toLowerCase()];
    
                return targetFields.some(field => field.startsWith(keyword));
            });
    
            console.log('Search Option:', searchOption);
            console.log('Search Keyword:', keyword);
            console.log('Filtered User List:', filtered);
    
            setFilteredUserList(filtered);
        }
    
        setCurrentPage(1);
    };

        // 사용자 삭제 함수
        const handleDeleteUser = async () => {
            if (!selectedUser) {
                return;
            }
        
            try {
                // API 호출하여 사용자 삭제
                const response = await fetch(`/api/user/${selectedUser.id}`, {
                    method: 'DELETE',
                });
        
                if (response.ok) {
                    // 삭제 성공 시 모달 닫기 및 사용자 목록 갱신
                    setModalOpen(false);
                    
                    // 페이지 새로고침
                    window.location.reload();
                } else {
                    console.error('사용자 삭제에 실패했습니다.');
                }
            } catch (error) {
                console.error('사용자 삭제 중 오류 발생:', error);
            }
        };
    

    return (
        <div>
        <AdminHeader />
        <div className="userManagePage">
            <div className='mypageWrap'>
                <div className="userManageSearch">
                    <div className="userManageSelect">
                    <select value={searchOption} onChange={handleSearchOptionChange}>
                            <option value="username">아이디</option>
                            <option value="hakbun">학번</option>
                            <option value="email">이메일</option>
                        </select>
                    </div>
                        <form>
                        <input
                            type="text"
                            className="form-control"
                            value={searchKeyword}
                            onChange={(e) => setSearchKeyword(e.target.value)}
                        />
                            <button type="submit" className="searchBtn" onClick={handleSearch}>
                                <img src={search} className="searchIcon" width="15px" height="15px" />
                            </button>
                        </form>
                    </div>
                <div class="userInfoWrap">
                    <div class="userInfoList">
                        <div class="top">
                            <div class="num">번호</div>
                            <div class="title">아이디</div>
                            <div class="writer">학번</div>
                            <div class="date">이메일</div>
                        </div>
                        {filteredUserList.slice().reverse().map((user, index) => ( 
                            <div key={index}>
                                <a onClick={() => {
                                        setSelectedUser(user);
                                        setModalOpen(true);
                                    }}>
                                    <div className="num">{userList.length - index}</div>
                                    <div className="title">{user.username}</div>
                                    <div className="writer">{user.hakbun}</div>
                                    <div className="date">{user.email}</div>
                                </a>
                                {modalOpen && selectedUser && (
                                        <div className={'managemodal-container'} ref={modalBackground} onClick={e => {
                                            if (e.target === modalBackground.current) {
                                                setModalOpen(false);
                                            }
                                        }}>
                                            <div className={'managemodal-content'}>
                                                <button className={'managemodal-close-btn'} onClick={() => setModalOpen(false)}>
                                                    <img src={close} width='15px' height='15px'/>
                                                </button>
                                                <div className='popupName'>사용자 정보</div>
                                                {/* 클릭한 사용자 정보를 이용하여 동적으로 표시 */}
                                                <img src={testuser} width='70px' height='70px' alt={selectedUser.username} />
                                                <div className='testUserName'>{selectedUser.username}</div>
                                                <div className='infoTable'>
                                                    <a className='info'>
                                                        <div className="infoName">아이디</div>
                                                        <div className="infoInfo">{selectedUser.username}</div>
                                                    </a>
                                                    <a className='info'>
                                                        <div className="infoName">학번</div>
                                                        <div className="infoInfo">{selectedUser.hakbun}</div>
                                                    </a>
                                                    <a className='info'>
                                                        <div className="infoName">이메일</div>
                                                        <div className="infoInfo">{selectedUser.email}</div>
                                                    </a>
                                                </div>
                                                <button className="deleteBtn2" onClick={handleDeleteUser}>
                                                    삭제
                                                </button>
                                            </div>
                                        </div>
                                    )}
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
        </div>
            <Footer/></div>
    );
};
export default UserManage;