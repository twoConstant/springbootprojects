import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';

import ArticleList from './components/ArticlesList';
import ArticleDetail from './components/ArticleDetail';
import ArticleEdit from './components/ArticleEdit';
import ArticleCre from './components/ArticleCre';
import CommentCre from './components/CommentCre';
import QuillEditor from './components/QuillEditor';

const App = () => {
  return (
    <Router>
      <Routes>
        <Route path="/articles" element={<ArticleList />} />
        <Route path="/articles/:article_id" element={<ArticleDetail />} />
        <Route path="/articles/:article_id/edit" element={<ArticleEdit />} />
        <Route path="/articles/create" element={<ArticleCre />} />
        <Route path="/articles/:article_id/comment-cre" element={<CommentCre />} />
        <Route path="/practices/quill" element={<QuillEditor />} />
      </Routes>
    </Router>
  )
}

export default App;
