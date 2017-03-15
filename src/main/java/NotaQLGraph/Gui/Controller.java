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

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import notaql.NotaQL;

import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    public TextArea queryArea;
    public Button executeButton;
    public ProgressIndicator indicator;
    public GridPane engineGrid;
    public Label statusLabel;


    public void initialize(URL location, ResourceBundle resources) {


    }

    public void execute(ActionEvent actionEvent) {
        System.out.println("tes");

        queryArea.setDisable(true);
        executeButton.setDisable(true);
        indicator.setVisible(true);
        final Instant startTime = Instant.now();
        final Runnable runnable = new Runnable() {

            public void run() {
                evaluate();

                indicator.setVisible(false);
                executeButton.setDisable(false);
                queryArea.setDisable(false);

                final Instant endTime = Instant.now();

                //    statusLabel.setText("Last execution took: " + Duration.between(startTime, endTime));
            }

        };
        new Thread(runnable).start();
    }

    private void evaluate() {
        String expression = this.queryArea.getText();


        try {
            NotaQLGraph.NotaQLGraph.notaqlGraph(expression);
        } catch (Exception e) {
        }
    }


}
