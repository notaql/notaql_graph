/*
 * Copyright 2015 by Thomas Lottermann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package NotaQLGraph.Gui;

import com.sun.javafx.PlatformUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import notaql.NotaQL;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialogs;

public class NotaQLGraphGui extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        // this makes text look nicer for some reason
        if(PlatformUtil.isLinux())
            System.setProperty("prism.lcdtext", "false");

        Parent root = FXMLLoader.load(getClass().getResource("/notaql.fxml"));
        primaryStage.setTitle("NotaQLGraph GUI");
        primaryStage.setScene(new Scene(root, 600, 450));
        primaryStage.setMinHeight(450);
        primaryStage.setMinWidth(600);
        primaryStage.show();



    }


    public static void main(String... args) {
        launch(args);
    }
}
