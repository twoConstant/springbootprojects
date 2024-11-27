import "quill/dist/quill.snow.css";
import Quill from "quill/core";

// Quill의 모듈과 테마를 임포트합니다.
import Toolbar from "quill/modules/toolbar";
import Snow from "quill/themes/snow";

// 포맷을 임포트합니다.
import Bold from "quill/formats/bold";
import Italic from "quill/formats/italic";
import Header from "quill/formats/header";

// Quill을 커스터마이즈하여 등록합니다.
Quill.register({
  "modules/toolbar": Toolbar,
  "themes/snow": Snow,
  "formats/bold": Bold,
  "formats/italic": Italic,
  "formats/header": Header,
});

export default Quill;