//package myblog.domain.test.dto.request;
//
//
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//public class TestUserDtoReq {
//    private Long id;
//    private String name;
//    private String username;
//    private String email;
//    private AddressDto address;
//    private String phone;
//    private String website;
//    private CompanyDto company;
//
//    // 중첩 클래스: AddressDto
//    @Data
//    @AllArgsConstructor
//    @NoArgsConstructor
//    @Builder
//    public static class AddressDto {
//        private String street;
//        private String suite;
//        private String city;
//        private String zipcode;
//        private GeoDto geo;
//    }
//
//    // 중첩 클래스: GeoDto
//    @Data
//    @AllArgsConstructor
//    @NoArgsConstructor
//    @Builder
//    public static class GeoDto {
//        private String lat;
//        private String lng;
//    }
//
//    // 중첩 클래스: CompanyDto
//    @Data
//    @AllArgsConstructor
//    @NoArgsConstructor
//    @Builder
//    public static class CompanyDto {
//        private String name;
//        private String catchPhrase;
//        private String bs;
//    }
//}
