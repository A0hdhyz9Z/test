/*
 *    ExpAIO.java
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import moa.options.ClassOption;
import moa.tasks.EvaluatePrequential;
import moa.tasks.MainTask;
import moa.tasks.TaskThread;

public class ExpAIO {

    static MainTask currentTask = new EvaluatePrequential();
    static MainTask currentTask2 = new EvaluatePrequential();
    static Writer writer;

    public ExpAIO() {

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

        int dsIdx = 0;
        int arrId = 0;
        String ens = "20";
        String theta = "0.99";
        String waitingTime = "90";

        /*** Use only for ORB ***/
        String paramsORB = "100;0.4;10;12;1.5;3";


        //		0
        //		0
        //		20
        //		0.99
        //		90
        //		100;0.4;10;12;1.5;3


        String[] datasetsArray = {"tomcat", "JGroups", "spring-integration",
                "camel", "brackets", "nova", "fabric8",
                "neutron", "npm", "BroadleafCommerce"
        };

        for (int i = 5; i <= 9; i++) {
            dsIdx = i;

            /*** OOB ***/
            String task = "CpjitsdpAIO3  -l (meta.ggc2.meta.WaitForLabelsOOB -i 15 -s " + ens + " -t " + theta + " -w " + waitingTime + ")  -s  (ArffFileStream -f (datasets/" + datasetsArray[dsIdx] + ".arff) -c 15) -e (FadingFactorEachClassPerformanceEvaluator -a 0.99) -f 1 -d results/" + datasetsArray[dsIdx] + "-AIO-OOB-" + arrId + ".csv";
//        String task = "CpjitsdpAIO2  -l (meta.ggc2.meta.WaitForLabelsOOB -i 15 -s " + ens + " -t " + theta + " -w " + waitingTime + ")  -s  (ArffFileStream -f (datasets/" + datasetsArray[dsIdx] + ".arff) -c 15) -e (FadingFactorEachClassPerformanceEvaluator -a 0.99) -f 1 -d results/" + datasetsArray[dsIdx] + "-AIO-OOB-" + arrId + ".csv";

            /*** ORB ***/
            String task2 = "CpjitsdpAIO -l (spdisc.meta.WFL_OO_ORB_Oza -i 15 -s " + ens + " -t " + theta + " -w " + waitingTime + " -p " + paramsORB + ")  -s  (ArffFileStream -f (datasets/" + datasetsArray[dsIdx] + ".arff) -c 15) -e (FadingFactorEachClassPerformanceEvaluator -a 0.99) -f 1 -d results/" + datasetsArray[dsIdx] + "-AIO-ORB-(" + paramsORB.replaceAll(";", "-") + ")-" + arrId + ".csv";

            ExecutorService executor = null; // 初始化executor
            try {
                // 创建一个包含两个线程的线程池
                executor = Executors.newFixedThreadPool(2);

                System.out.println(task);
                currentTask = (MainTask) ClassOption.cliStringToObject(task, MainTask.class, null);
                TaskThread thread = new TaskThread((moa.tasks.Task) currentTask);

                // 启动第一个线程
                Future<?> future1 = executor.submit(thread);

                // 等待第一个线程结束
                future1.get();

//                System.out.println(task2);
//                currentTask2 = (MainTask) ClassOption.cliStringToObject(task2, MainTask.class, null);
//                TaskThread thread2 = new TaskThread((moa.tasks.Task) currentTask2);
//
//                // 启动第二个线程
//                Future<?> future2 = executor.submit(thread2);
//
//                // 等待第二个线程结束
//                future2.get();
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (executor != null) {
                    executor.shutdown(); // 关闭ExecutorService，以释放资源
                }
            }

        }

//        /*** OOB ***/
//        String task = "CpjitsdpAIO  -l (meta.ggc2.meta.WaitForLabelsOOB -i 15 -s " + ens + " -t " + theta + " -w " + waitingTime + ")  -s  (ArffFileStream -f (datasets/" + datasetsArray[dsIdx] + ".arff) -c 15) -e (FadingFactorEachClassPerformanceEvaluator -a 0.99) -f 1 -d results/" + datasetsArray[dsIdx] + "-AIO-OOB-" + arrId + ".csv";
////        String task = "CpjitsdpAIO2  -l (meta.ggc2.meta.WaitForLabelsOOB -i 15 -s " + ens + " -t " + theta + " -w " + waitingTime + ")  -s  (ArffFileStream -f (datasets/" + datasetsArray[dsIdx] + ".arff) -c 15) -e (FadingFactorEachClassPerformanceEvaluator -a 0.99) -f 1 -d results/" + datasetsArray[dsIdx] + "-AIO-OOB-" + arrId + ".csv";
//
//        /*** ORB ***/
//        String task2 = "CpjitsdpAIO -l (spdisc.meta.WFL_OO_ORB_Oza -i 15 -s " + ens + " -t " + theta + " -w " + waitingTime + " -p " + paramsORB + ")  -s  (ArffFileStream -f (datasets/" + datasetsArray[dsIdx] + ".arff) -c 15) -e (FadingFactorEachClassPerformanceEvaluator -a 0.99) -f 1 -d results/" + datasetsArray[dsIdx] + "-AIO-ORB-(" + paramsORB.replaceAll(";", "-") + ")-" + arrId + ".csv";
//
//        ExecutorService executor = null; // 初始化executor
//        try {
//            // 创建一个包含两个线程的线程池
//            executor = Executors.newFixedThreadPool(2);
//
//            System.out.println(task);
//            currentTask = (MainTask) ClassOption.cliStringToObject(task, MainTask.class, null);
//            TaskThread thread = new TaskThread((moa.tasks.Task) currentTask);
//
//            // 启动第一个线程
//            Future<?> future1 = executor.submit(thread);
//
//            // 等待第一个线程结束
//            future1.get();
//
//            System.out.println(task2);
//            currentTask2 = (MainTask) ClassOption.cliStringToObject(task2, MainTask.class, null);
//            TaskThread thread2 = new TaskThread((moa.tasks.Task) currentTask2);
//
//            // 启动第二个线程
//            Future<?> future2 = executor.submit(thread2);
//
//            // 等待第二个线程结束
//            future2.get();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        } finally {
//            if (executor != null) {
//                executor.shutdown(); // 关闭ExecutorService，以释放资源
//            }
//        }


    }

}
