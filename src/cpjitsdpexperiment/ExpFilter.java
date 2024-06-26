/*
 *    ExpFilter.java
 *
 *    Copyright (C) 2022 University of Birmingham, Birmingham, United Kingdom
 *    @author Sadia Tabassum (sxt901@student.bham.ac.uk)
 *
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */

package cpjitsdpexperiment;

import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.TimeUnit;

import moa.options.ClassOption;
import moa.tasks.EvaluatePrequential;
import moa.tasks.MainTask;
import moa.tasks.TaskThread;

public class ExpFilter {

    static MainTask currentTask = new EvaluatePrequential();
    static MainTask currentTask2 = new EvaluatePrequential();
    static Writer writer;

    public ExpFilter() {

    }

    private static String[] savedArgs;

    public static String[] getArgs() {
        return savedArgs;
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        savedArgs = args;

//		int dsIdx = new Integer(args[0]);
//		int arrId = new Integer(args[1]);
//		String ens =  args[2];
//		String theta = args[3];
//		String waitingTime= args[4];
//		String filterParams = args[5];
        /*** Use only for ORB ***/
//        String paramsORB=args[6];

        int dsIdx = 0;
        int arrId = 0;
        String ens = "20";
        String theta = "0.99";
        String waitingTime = "90";
        String filterParams = "500;50;0.7;500";
        String paramsORB = "100;0.4;10;12;1.5;3";

        //		0
        //		0
        //		20
        //		0.99
        //		90
        //		500;50;0.7;500
        //		100;0.4;10;12;1.5;3

        String[] datasetsArray = {"tomcat", "JGroups", "spring-integration",
                "camel", "brackets", "nova", "fabric8",
                "neutron", "npm", "BroadleafCommerce"
        };


        /*** OOB ***/
        String task = "CpjitsdpFiltering  -l (meta.ggc2.meta.WaitForLabelsOOB -i 15 -s " + ens + " -t " + theta + " -w " + waitingTime + ")  -s  (ArffFileStream -f (datasets/" + datasetsArray[dsIdx] + ".arff) -c 15) -e (FadingFactorEachClassPerformanceEvaluator -a 0.99) -f 1 -d results/" + datasetsArray[dsIdx] + "-Filter-OOB-" + filterParams.replaceAll(";", "-") + "-" + arrId + ".csv";

        /*** ORB ***/
        String task2 = "CpjitsdpFiltering -l (spdisc.meta.WFL_OO_ORB_Oza -i 15 -s " + ens + " -t " + theta + " -w " + waitingTime + " -p " + paramsORB + ")  -s  (ArffFileStream -f (datasets/" + datasetsArray[dsIdx] + ".arff) -c 15) -e (FadingFactorEachClassPerformanceEvaluator -a 0.99) -f 1 -d results/" + datasetsArray[dsIdx] + "-Filter-ORB-(" + paramsORB.replaceAll(";", "-") + ")-" + filterParams.replaceAll(";", "-") + "-" + arrId + ".csv";


        try {
            System.out.println(task);
            currentTask = (MainTask) ClassOption.cliStringToObject(task, MainTask.class, null);
            TaskThread thread = new TaskThread((moa.tasks.Task) currentTask);

            thread.start();
            thread.join();  // 等待第一个线程结束

            System.out.println(task2);
            currentTask2 = (MainTask) ClassOption.cliStringToObject(task2, MainTask.class, null);
            TaskThread thread2 = new TaskThread((moa.tasks.Task) currentTask2);

            thread2.start();  // 在第一个线程结束后启动第二个线程
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
