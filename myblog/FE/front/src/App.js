import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';

import ArticleList from './components/ArticlesList';
import ArticleDetail from './components/ArticleDetail';
import ArticleEdit from './components/ArticleEdit';
import ArticleCre from './components/ArticleCre';
import CommentCre from './components/CommentCre';

const App = () => {
  return (
    <Router>
      <Routes>
        <Route path="/articles" element={<ArticleList />} />
        <Route path="/articles/:article_id" element={<ArticleDetail />} />
        <Route path="/articles/:article_id/edit" element={<ArticleEdit />} />
        <Route path="/articles/create" element={<ArticleCre />} />
        <Route path="/articles/:article_id/comment-cre" element={<CommentCre />} />
      </Routes>
    </Router>
  )
}

export default App;
