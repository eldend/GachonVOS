import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useParams } from 'react-router-dom';
import './inside.css';
import Header from '../main/Header';
import Footer from '../main/Footer';

export default function Inside() {
    const { postId } = useParams();
    const [post, setPost] = useState({});
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchPostData = async () => {
            try {
                const postApiUrl = `http://localhost:8080/api/post/${postId}`;
                const response = await axios.get(postApiUrl);
                const postData = response.data;
                setPost(postData);
                setLoading(false);
            } catch (error) {
                console.error('포스트 데이터를 불러오는 중 오류 발생:', error);
                setError('데이터를 가져오는 중 에러가 발생했습니다.');
            }
        };

        fetchPostData();
    }, [postId]);

    if (loading) {
        return <div>로딩 중...</div>;
    }

    if (error) {
        return <div>에러: {error}</div>;
    }

    console.log(post);
    console.log(loading);
    console.log(error);

    return (
        <div>
            <Header />
            <div className="insidePage">
                <div className="insideTitleWrap">
                    <div className="insideTitle">{post.title}</div>
                </div>
                <div className="insideDetailWrap">
                    <div className="insideDetail">
                        {post.content}
                        <div className="insideAdd">{post.createdDate}</div>
                    </div>
                </div>
                <div className="insideManagerWrap">
                    <div className="insideManager">건의 처리 상태</div>
                    <div className="managerBoxWrap">
                        <div className="managerBox">{post.processstate}</div>
                    </div>
                </div>
                <div className="managerDetailWrap">
                    <div className="managerInfo">
                        <div className="managerPartWrap">
                            <div className="managerPart">담당 부서 : </div>
                            {/*<div className="managerTeam">{post.comments[0].dept}</div>*/}
                            {post.comments.map((comment, index) => (
                                <div className="managerTeam" key={index}>
                                    {comment.dept}
                                </div>
                            ))}
                        </div>
                        <div className="managerPartWrap">
                            <div className="managerPart">연락처 : </div>
                            {/*<div className="managerTeam">{post.comments[0].tel}</div>*/}
                            {post.comments.map((comment, index) => (
                                <div className="managerTeam" key={index}>
                                    {comment.tel}
                                </div>
                            ))}
                        </div>
                    </div>
                    <div className="managerComment">
                        {/* post.comments가 배열이라면 map 함수를 사용하여 각 아이템을 렌더링할 수 있습니다. */}
                        {post.comments.map((comment) => (
                            <div key={comment.id}>{comment.comment}</div>
                        ))}
                    </div>
                </div>
                <div className="managerDate">{post.modifiedDate}</div>
            </div>
            <Footer />
        </div>
    );
}