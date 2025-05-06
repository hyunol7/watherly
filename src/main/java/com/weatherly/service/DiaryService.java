package com.weatherly.service;

import com.weatherly.domain.Diary;
import com.weatherly.repository.DiaryRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class DiaryService {

    @Value("${openweathermap.key}") // application.properties나 환경 변수에서 API 키 값을 주입받음
    private String apikey;

    private final DiaryRepository diaryRepository;

    public DiaryService(DiaryRepository diaryRepository) {
        this.diaryRepository = diaryRepository;
    }

    // 일기를 작성하는 메서드. 날짜와 일기 내용을 입력받음
    public void createDiary(LocalDate date, String text) {
        // 현재 날씨 데이터를 가져오는 내부 메서드 호출
        String weatherData = getWeatherString();

        //받아온 날씨 json 파싱하기
        Map<String, Object> parseWeather = parseWeather(weatherData);

        //파싱된 데이터 + 일기 값 db에 넣기
        Diary diary = new Diary();
        diary.setWeather(parseWeather.get("main").toString());
        diary.setIcon(parseWeather.get("icon").toString());
        diary.setTemperature((double) parseWeather.get("temp"));
        diary.setText(text);
        diary.setDate(date);
        diaryRepository.save(diary);
     }

     public List<Diary> readDiary(LocalDate date) {
        return diaryRepository.findAllByDate(date);
     }

     public List<Diary> readDiaries(LocalDate startDate, LocalDate endDate) {
        return diaryRepository.findAllByDateBetween(startDate, endDate);
     }

     public void updateDiary(LocalDate date, String text) {
        Diary newDiary = diaryRepository.getFirstByDate(date);
        newDiary.setText(text);
        diaryRepository.save(newDiary);
     }

     public void deleteDiary(LocalDate date){
        diaryRepository.deleteByDate(date);
     }

    // 외부 OpenWeatherMap API를 호출해서 날씨 정보를 문자열로 받아오는 메서드
    private String getWeatherString(){
        // OpenWeatherMap API의 호출 URL 생성 (도시: 서울, API 키 포함)
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=seoul&appid=" + apikey;

        try {
            // URL 객체 생성 및 연결 초기화
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // HTTP 요청 메서드 설정 (GET 방식)
            connection.setRequestMethod("GET");

            // 응답 코드 확인 (200: 성공)
            int responseCode = connection.getResponseCode();

            BufferedReader br;
            if (responseCode == 200) {
                // 정상 응답일 경우 입력 스트림에서 읽기
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                // 오류 응답일 경우 에러 스트림에서 읽기
                br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }

            // 응답 내용을 한 줄씩 읽어 StringBuilder에 저장
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }

            br.close(); // 스트림 닫기

            // 전체 응답 문자열 반환 (JSON 형식 문자열)
            return response.toString();

        } catch (Exception e) {
            // 예외 발생 시 실패 메시지 반환
            return "failed to get response";
        }
    }

    private Map<String, Object> parseWeather(String jsonString) {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject;

        try{
            jsonObject = (JSONObject) parser.parse(jsonString);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        Map<String, Object> resultMap = new HashMap<>();

        JSONObject mainObject = (JSONObject) jsonObject.get("main");
        JSONArray weatherArray = (JSONArray) jsonObject.get("weather");
        JSONObject weatherData = (JSONObject) weatherArray.get(0);

        resultMap.put("temperature", mainObject.get("temp"));
        resultMap.put("main", weatherData.get("main"));
        resultMap.put("icon", weatherData.get("icon"));
        return resultMap;
    }
}
