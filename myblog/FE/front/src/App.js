import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';

import ArticleList from './components/ArticlesList';
import ArticleDetail from './components/ArticleDetail';

const App = () => {
  return (
    <Router>
      <Routes>
        <Route path="" element={<ArticleList />} />
        <Route path="/articles/:article_id" element={<ArticleDetail />} />
      </Routes>
    </Router>
  )
}

export default App;
