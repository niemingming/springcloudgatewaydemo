package com.nmm.study;

public class RegTest {

    public static void main(String[] args) {
        String path = "/abc/defwe/wefwer/1?name=daweef";
        String npath = path.replaceAll("/abc/(?<pos>.*)","/${pos}");
        System.out.println(npath);
        path = path.replaceAll("/abc/(?<pos>.*)","/\\k<pos>");
        System.out.println(path);
    }
}
