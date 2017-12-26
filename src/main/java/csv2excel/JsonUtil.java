package csv2excel;

import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.collect.Lists;
/**
 * json工具，暴露两个方法，获取json的所有key和美化json
 * @author doujinlong
 *
 */
public class JsonUtil {

	public static int testIsArrayORObject(String sJSON) {
		/*
		 * return 0:既不是array也不是object return 1：是object return 2 ：是Array
		 */
		try {
			new JSONArray(sJSON);
			return 2;
		} catch (JSONException e) {// 抛错 说明JSON字符不是数组或根本就不是JSON
			try {
				new JSONObject(sJSON);
				return 1;
			} catch (JSONException e2) {// 抛错 说明JSON字符根本就不是JSON
				return 0;
			}
		}

	}

	public static List<String> getKeys(JSONObject test) throws Exception {
		List<String> ret = Lists.newArrayList();
		Iterator<String> keys = test.keys();
		while (keys.hasNext()) {
			try {
				String key = keys.next().toString();
				String value = test.optString(key);
				int i = testIsArrayORObject(value);
				if (i == 0) {
					ret.add(key);
				} else if (i == 1) {
					List<String> temp = getKeys(new JSONObject(value));
					List<String> keySon = Lists.newArrayList();
					temp.forEach(t -> keySon.add(key + "." + t));
					ret.addAll(keySon);
				} else if (i == 2) {
					JSONArray arrays = new JSONArray(value);
					for (int k = 0; k < arrays.length();) {
						JSONObject array = new JSONObject(arrays.get(k).toString());
						List<String> temp = getKeys(array);
						List<String> keySon = Lists.newArrayList();
						temp.forEach(t -> keySon.add(key + ":[{" + t + "}]"));
						ret.addAll(keySon);
						break;
					}
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return ret;
	}

	/**
	 * 单位缩进字符串。
	 */
	private static String SPACE = "   ";

	/**
	 * 返回格式化JSON字符串。
	 * 
	 * @param json
	 *            未格式化的JSON字符串。
	 * @return 格式化的JSON字符串。
	 */
	public static String formatJson(String json) {
		StringBuffer result = new StringBuffer();

		int length = json.length();
		int number = 0;
		char key = 0;
		// 遍历输入字符串。
		for (int i = 0; i < length; i++) {
			// 1、获取当前字符。
			key = json.charAt(i);

			// 2、如果当前字符是前方括号、前花括号做如下处理：
			if ((key == '[') || (key == '{')) {
				// （1）如果前面还有字符，并且字符为“：”，打印：换行和缩进字符字符串。
				if ((i - 1 > 0) && (json.charAt(i - 1) == ':')) {
					result.append('\n');
					result.append(indent(number));
				}

				// （2）打印：当前字符。
				result.append(key);

				// （3）前方括号、前花括号，的后面必须换行。打印：换行。
				result.append('\n');

				// （4）每出现一次前方括号、前花括号；缩进次数增加一次。打印：新行缩进。
				number++;
				result.append(indent(number));

				// （5）进行下一次循环。
				continue;
			}

			// 3、如果当前字符是后方括号、后花括号做如下处理：
			if ((key == ']') || (key == '}')) {
				// （1）后方括号、后花括号，的前面必须换行。打印：换行。
				result.append('\n');

				// （2）每出现一次后方括号、后花括号；缩进次数减少一次。打印：缩进。
				number--;
				result.append(indent(number));

				// （3）打印：当前字符。
				result.append(key);

				// （4）如果当前字符后面还有字符，并且字符不为“，”，打印：换行。
				if (((i + 1) < length) && (json.charAt(i + 1) != ',')) {
					result.append('\n');
				}

				// （5）继续下一次循环。
				continue;
			}

			// 4、如果当前字符是逗号。逗号后面换行，并缩进，不改变缩进次数。
			if ((key == ',')) {
				result.append(key);
				result.append('\n');
				result.append(indent(number));
				continue;
			}

			// 5、打印：当前字符。
			result.append(key);
		}

		return result.toString();
	}

	/**
	 * 返回指定次数的缩进字符串。每一次缩进三个空格，即SPACE。
	 * 
	 * @param number
	 *            缩进次数。
	 * @return 指定缩进次数的字符串。
	 */
	private static String indent(int number) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < number; i++) {
			result.append(SPACE);
		}
		return result.toString();
	}
}