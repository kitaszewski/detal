package pl.rawinet.detal.utils;

import org.springframework.stereotype.Component;

@Component
public class PolishCharsRemover {

    private StringBuilder sb;

    public PolishCharsRemover() {
        sb = new StringBuilder();
    }

    public String removePlChars(String in){

        int l = in.length();
        for (int i = 0; i < l; i++)
        {
            switch (in.charAt(i))
            {
                case 'ą': sb.append('a'); break;
                case 'ć': sb.append('c'); break;
                case 'ę': sb.append('e'); break;
                case 'ł': sb.append('l'); break;
                case 'ń': sb.append('n'); break;
                case 'ó': sb.append('o'); break;
                case 'ś': sb.append('s'); break;
                case 'ź': sb.append('z'); break;
                case 'ż': sb.append('z'); break;
                case 'Ą': sb.append('A'); break;
                case 'Ć': sb.append('C'); break;
                case 'Ę': sb.append('E'); break;
                case 'Ł': sb.append('L'); break;
                case 'Ń': sb.append('N'); break;
                case 'Ó': sb.append('O'); break;
                case 'Ś': sb.append('S'); break;
                case 'Ź': sb.append('Z'); break;
                case 'Ż': sb.append('Z'); break;
                case ' ': sb.append('_'); break;
                case '\'': break;
                case '\"': break;
                default: sb.append(in.charAt(i)); break;
            }
        }
        return sb.toString();
    }
}
