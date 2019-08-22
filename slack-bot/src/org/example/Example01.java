package org.example;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.riversun.slacklet.Slacklet;
import org.riversun.slacklet.SlackletRequest;
import org.riversun.slacklet.SlackletResponse;
import org.riversun.slacklet.SlackletService;
import org.riversun.xternal.simpleslackapi.SlackChannel;

public class Example01 {

	public static void main(String[] args) throws IOException {

		String botToken = ResourceBundle.getBundle("credentials").getString("slack.bot_api_token");

		SlackletService slackService = new SlackletService(botToken);

		//改行
		String br = System.getProperty("line.separator");

		//-------------------------クイズ格納
		List<String> quizlists = new ArrayList<String>();
		quizlists.add("アメリカの首都は？" + br + "1:ワシントン" + br + "2:ニューヨーク" + br + "3:ロサンゼルス");
		quizlists.add("私はだれ？");
		quizlists.add("日本で一番大きい県は？" + br + "1:岩手県" + br + "2:福島県" + br + "3:長野県");

		// slackletを追加する
		slackService.addSlacklet(new Slacklet() {
			int mode = 0;

			@Override
			public void onMessagePosted(SlackletRequest req, SlackletResponse resp) {
				// メッセージがユーザーからポストされた

				System.out.println("mode:" + mode);
				System.out.println(req.getUserDisp());

				// メッセージがポストされたチャンネルを取得する
				SlackChannel channel = req.getChannel();

				if ("test_matsunami".equals(channel.getName())) {
					// #test_matsunamiチャンネルだった場合

					// メッセージ本文を取得
					String content = req.getContent();

					//-----------------------------------------------------------クイズ出題
					if (mode == 1) {
						Collections.shuffle(quizlists);
						String quiz = quizlists.get(0);
						resp.reply(quiz);
						resp.reply("答えを番号で入力してください");
						mode = 2;
					} else if (mode == 2) {//----------------------------------------答え合わせ
						if (content.equals("1") || content.equals("１")) {
							resp.reply("正解");
							resp.reply("クイズを続けますか？" + br + "1:続ける" + br + "2:終わる");
							mode = 3;
						} else if (content.equals("2") || content.equals("２")) {
							resp.reply("不正解");
							resp.reply("クイズを続けますか？" + br + "1:続ける" + br + "2:終わる");
							mode = 3;
						} else if (content.equals("3") || content.equals("３")) {
							resp.reply("不正解");
							resp.reply("クイズを続けますか？" + br + "1:続ける" + br + "2:終わる");
							mode = 3;
						} else {
							resp.reply("1,2,3から選択してください");
						}
					} else if (mode == 3) {
						if (content.equals("1") || content.equals("１")) {
							resp.reply("続けます");
							Collections.shuffle(quizlists);
							String quiz = quizlists.get(0);
							resp.reply(quiz);
							resp.reply("答えを番号で入力してください");
							mode = 2;
						}
						else if (content.equals("2") || content.equals("２")) {
							mode = 0;
							resp.reply("終わります");
						}
					} else if (mode == 0) {

						//------------------------------------------時間を表示
						if (content.contentEquals("時間")) {
							Date date = new Date();
							SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy'年'MM'月'dd'日'E'曜日'k'時'mm'分'ss'秒'");
							resp.reply("現在の時刻は" + dateFormat.format(date) + "です");
						}

						//------------------------------------------クイズモードにチェンジ
						else if (content.contentEquals("クイズ")) {
							mode = 2;
							resp.reply("クイズを始めます！");
							Collections.shuffle(quizlists);
							String quiz = quizlists.get(0);
							resp.reply(quiz);
							resp.reply("答えを番号で入力してください");
						}
					}
				}
			}
		});
		// slackletserviceを開始(slackに接続)
		slackService.start();
	}
}

