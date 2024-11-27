import React, { useEffect, useRef, forwardRef, useImperativeHandle } from "react";
import Quill from "../config/Quill"; // 커스터마이즈된 Quill 객체를 가져옵니다.
import "quill/dist/quill.snow.css"; // 스노우 테마 CSS 파일을 가져옵니다.

const QuillEditor = forwardRef((props, ref) => {
  const editorRef = useRef(null);
  const quillInstance = useRef(null);

  useEffect(() => {
    quillInstance.current = new Quill(editorRef.current, {
      theme: "snow",
      modules: {
        toolbar: [
          [{ header: [1, 2, false] }],
          ["bold", "italic", "underline"],
          [{ list: "ordered" }, { list: "bullet" }],
          ["link", "image"],
          ["clean"], // 포맷 지우기 버튼
        ],
      },
    });
  }, []);

  // 부모 컴포넌트에서 quillInstance에 접근할 수 있도록 설정
  useImperativeHandle(ref, () => ({
    getQuillInstance: () => quillInstance.current,
  }));

  return (
    <div>
      <div ref={editorRef} style={{ height: "300px" }} /> {/* Quill이 붙을 컨테이너 */}
    </div>
  );
});

export default QuillEditor;
