package com.zmt51.top;

import com.zmt51.top.bean.Multicast;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


public class GetHtml {

    private static String ip = "dubaiyu.f3322.net";

    public static void main(String[] args) throws IOException {
        setIp(args);
        List<Multicast> list = new LinkedList<>();
        Document doc = Jsoup.connect("http://epg.51zmt.top:8000/sctvmulticast.html").get();
        Elements allElements = doc.getElementsByTag("tr");
        for (int i = 1; i < allElements.size(); i++) {
            list.add(analysisTr(allElements.get(i)));
        }
        list = list.stream().filter(s -> !"未能播放".equals(s.getVideParameter())).collect(Collectors.toList());

        String returnString = getReturnString(list);
        writeStringToFile("./", "iptv-" + ip + ".m3u", returnString);
    }


    private static void setIp(String[] args) {
        if (args.length == 1) {
            String arg = args[0];
            if (arg.startsWith("ip=")) {
                ip = arg.split("=")[1];
            }
        }
    }

    /**
     * 把字符串写到制定目录下的指定文件中去
     *
     * @param path     文件存放路径
     * @param fileName 文件名称
     * @param content  文件内容
     * @return
     */
    public static boolean writeStringToFile(String path, String fileName, String content) {

        try (FileOutputStream fileOutputStream = new FileOutputStream(path + File.separator + fileName, true);
             OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
             BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter)) {
            bufferedWriter.write(content);
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 把List<Lzu6Body>转换成文件对象String字符串
     *
     * @param list
     * @return
     */
    private static String getReturnString(List<Multicast> list) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("#EXTM3U");
        for (Multicast entity : list) {
            stringBuffer.append(entity.toRow(ip));
        }
        return stringBuffer.toString();
    }

    private static Multicast analysisTr(Element tr) {
        //获取td节点 页面存在7列
        Elements td = tr.getElementsByTag("td");
        if (7 != td.size()) {
            throw new RuntimeException("html 结构已变动当前程序不支持");
        }
        int column = 1;//自定义列号
        Multicast multicast = new Multicast();
        for (Element e : td) {
            if (e.childNodeSize() == 1 && column != 6) {  //只有一个节点 且不是视频编码
                String node = e.childNode(0).toString();
                switch (column) {
                    case 1:
                        multicast.setIndex(Integer.parseInt(node));
                        break;
                    case 2:
                        multicast.setChannelName(node);
                        break;
                    case 3:
                        multicast.setMulticast(node);
                        break;
                    case 4:
                        multicast.setPlaybackDays(Integer.parseInt(node));
                        break;
                    case 5:
                        multicast.setChannelId(Long.parseLong(node));
                        break;
                    default:
                        multicast.setPlaybackAddress(node);
                        break;
                }
                column++; //自定义节点下一个序号
            } else if (6 == column && (e.childNodeSize() == 3 || e.childNodeSize() == 1)) { //处理视频格式
                List<String> videParameterList = new ArrayList<>(3);
                Elements em = e.getElementsByTag("em");
                for (Element m : em) {
                    videParameterList.add(m.childNode(0).toString());
                }
                multicast.setVideParameter(String.join("/", videParameterList));
                column++;  //自定义节点下一个序号
            } else {
                throw new RuntimeException("html 结构已变动当前程序不支持");
            }
        }
        return multicast;
    }
}
