package server;

/**
 * 문제의 정답
 */
public class Questions {
    String[] answer = new String[50];

    public Questions() {
        answer[1] = "카레이서";
        answer[2] = "조선소";
        answer[3] = "사파이어";
        answer[4] = "물안개";
        answer[5] = "도시";
        answer[6] = "김경호";
        answer[7] = "장갑차";
        answer[8] = "산모";
        answer[9] = "가로수";
        answer[10] = "귓속말";
        answer[11] = "쥐불놀이";
        answer[12] = "샴푸";
        answer[13] = "진심";
        answer[14] = "장구벌레";
        answer[15] = "우거지";
        answer[16] = "게릴라";
        answer[16]    = "경찰";
        answer[17]    = "대통령";
        answer[18]    = "대학교";
        answer[19]    = "박지성";
        answer[20]    = "류현진";
        answer[21]    = "스티브잡스";
        answer[22]    = "미국";
        answer[23]    = "프랑스";
        answer[24]    = "김치찌개";
        answer[25]    = "조선소";
        answer[26]    = "여학생";
        answer[27]    = "코흘리개";
        answer[28]    = "외양간";
        answer[29]    = "유관순";
        answer[30]    = "원빈";
        answer[31]    = "기독교";
        answer[32]    = "군대";
        answer[33]    = "찹쌀떡";
        answer[34]    = "늑대";
        answer[35]    = "소녀시대";
        answer[36]    = "솜사탕";
        answer[37]    = "소개팅";
        answer[38]    = "여자친구";
        answer[39]    = "싸이";
        answer[40]    = "수족관";
        answer[41]    = "공무원";
        answer[42]    = "놀부";
        answer[43]    = "신동엽";
        answer[44]    = "해운대";
        answer[45]    = "밥도둑";
    }

    // 랜덤으로 하나의 문제를 리턴한다.
    public String setQuestion() {
        int stage = (int) (Math.random() * 45) + 1;
        return answer[stage];
    }
}
