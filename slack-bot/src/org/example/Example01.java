package org.example;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
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

		DBConnector dbConnector = new DBConnector();
		Connection connection = dbConnector.getConnection();
		String sql = "SELECT * FROM q ORDER BY RAND() LIMIT 1";


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

				if ("random".equals(channel.getName())) {
					// #randomチャンネルだった場合

					// メッセージ本文を取得
					String content = req.getContent();

					//-----------------------------------------------------------クイズ出題
					if (mode == 1) {
						try {
							PreparedStatement preparedStatement = connection.prepareStatement(sql);
							ResultSet rs = preparedStatement.executeQuery();
							while(rs.next()) {
								resp.reply(rs.getString("question"));
								resp.reply("答えを番号で入力してください");
										}
							}catch(Exception e) {
		            			e.printStackTrace();
		            			}
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
							try {
								PreparedStatement preparedStatement = connection.prepareStatement(sql);
								ResultSet rs = preparedStatement.executeQuery();
								while(rs.next()) {
									resp.reply(rs.getString("question"));
									resp.reply("答えを番号で入力してください");
											}
								}catch(Exception e) {
			            			e.printStackTrace();
			            			}
							mode = 2;
						} else if (content.equals("2") || content.equals("２")) {
							mode = 0;
							resp.reply("終わります");
						} else {
							resp.reply("1,2から選択してください");
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
							
							try {
								PreparedStatement preparedStatement = connection.prepareStatement(sql);
								ResultSet rs = preparedStatement.executeQuery();
								while(rs.next()) {
									resp.reply(rs.getString("question"));
									resp.reply("答えを番号で入力してください");
											}
								}catch(Exception e) {
			            			e.printStackTrace();
			            			}




						}
					}
				}
			}
		});
		// slackletserviceを開始(slackに接続)
		slackService.start();
	}
}

