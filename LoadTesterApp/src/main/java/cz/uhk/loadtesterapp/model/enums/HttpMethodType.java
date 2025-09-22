package cz.uhk.loadtesterapp.model.enums;

public enum HttpMethodType {
    GET, POST, PUT, DELETE, PATCH, HEAD, OPTIONS;

    public org.springframework.http.HttpMethod toSpring() {
        return org.springframework.http.HttpMethod.valueOf(this.name());
    }
}
