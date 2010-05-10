/*
   Copyright (C) 2003 MySQL AB

      This program is free software; you can redistribute it and/or modify
      it under the terms of the GNU General Public License as published by
      the Free Software Foundation; either version 2 of the License, or
      (at your option) any later version.

      This program is distributed in the hope that it will be useful,
      but WITHOUT ANY WARRANTY; without even the implied warranty of
      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
      GNU General Public License for more details.

      You should have received a copy of the GNU General Public License
      along with this program; if not, write to the Free Software
      Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

 */
package testsuite.regression;

import testsuite.BaseTestCase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;


/**
 * Tests for multi-thread stress regressions.
 *
 * @author Mark Matthews
 * @version $Id: StressRegressionTest.java,v 1.1.2.4 2004/04/28 20:16:40 mmatthew Exp $
 */
public class StressRegressionTest extends BaseTestCase {
    private int numThreadsStarted;

    /**
     * Creates a new StressRegressionTest
     *
     * @param name the name of the test.
     */
    public StressRegressionTest(String name) {
        super(name);

        // TODO Auto-generated constructor stub
    }

    /**
     * Runs all test cases in this test suite
     *
     * @param args
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(StressRegressionTest.class);
    }

    /**
     *
     *
     * @throws Exception ...
     */
    public synchronized void testContention() throws Exception {
    	if (false) {
        System.out.println("Calculating baseline elapsed time...");

        long start = System.currentTimeMillis();

        contentiousWork(this.conn, this.stmt, 0);

        long singleThreadElapsedTimeMillis = System.currentTimeMillis() - start;

        System.out.println("Single threaded execution took "
            + singleThreadElapsedTimeMillis + " ms.");

        int numThreadsToStart = 95;

        System.out.println("\nStarting " + numThreadsToStart + " threads.");

        numThreadsStarted = numThreadsToStart;

        ContentionThread[] threads = new ContentionThread[numThreadsStarted];

        for (int i = 0; i < numThreadsToStart; i++) {
            threads[i] = new ContentionThread(i);
            threads[i].start();
        }

        for (;;) {
            try {
                wait();

                if (numThreadsStarted == 0) {
                    break;
                }
            } catch (InterruptedException ie) {
                // ignore
            }
        }

        // Collect statistics...
        System.out.println("Done!");

        double avgElapsedTimeMillis = 0;

        List elapsedTimes = new ArrayList();

        for (int i = 0; i < numThreadsToStart; i++) {
            elapsedTimes.add(new Long(threads[i].elapsedTimeMillis));

            avgElapsedTimeMillis += ((double) threads[i].elapsedTimeMillis / numThreadsToStart);
        }

        Collections.sort(elapsedTimes);

        System.out.println("Average elapsed time per-thread was "
            + avgElapsedTimeMillis + " ms.");
        System.out.println("Median elapsed time per-thread was "
            + elapsedTimes.get(elapsedTimes.size() / 2) + " ms.");
        System.out.println("Minimum elapsed time per-thread was "
            + elapsedTimes.get(0) + " ms.");
        System.out.println("Maximum elapsed time per-thread was "
            + elapsedTimes.get(elapsedTimes.size() - 1) + " ms.");
    	}
    }

    /**
     *
     *
     * @throws Exception ...
     */
    public void testCreateConnections() throws Exception {
        new CreateThread().run();
    }

    /**
     *
     *
     * @throws Exception ...
     */
    public void testCreateConnectionsUnderLoad() throws Exception {
        new CreateThread(new BusyThread()).run();
    }

    void contentiousWork(Connection threadConn, Statement threadStmt,
        int threadNumber) {
        Date now = new Date();

        try {
            for (int i = 0; i < 1000; i++) {
                ResultSet threadRs = threadStmt.executeQuery("SELECT 1, 2");

                while (threadRs.next()) {
                    threadRs.getString(1);
                    threadRs.getString(2);
                }

                threadRs.close();

                PreparedStatement pStmt = threadConn.prepareStatement(
                        "SELECT ?");
                pStmt.setTimestamp(1, new Timestamp(now.getTime()));

                threadRs = pStmt.executeQuery();

                while (threadRs.next()) {
                    threadRs.getTimestamp(1);
                }

                threadRs.close();
                pStmt.close();
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex.toString());
        }
    }

    synchronized void reportDone() {
        this.numThreadsStarted--;
        notify();
    }

    public class BusyThread extends Thread {
        boolean stop = false;

        public void run() {
            while (!stop) {
            }
        }
    }

    class ContentionThread extends Thread {
        Connection threadConn;
        Statement threadStmt;
        int threadNumber;
        long elapsedTimeMillis;

        public ContentionThread(int num) throws SQLException {
            threadNumber = num;
            threadConn = getConnectionWithProps(new Properties());
            threadStmt = threadConn.createStatement();

            System.out.println(threadConn);
        }

        public void run() {
            long start = System.currentTimeMillis();

            try {
                contentiousWork(threadConn, threadStmt, threadNumber);
                elapsedTimeMillis = System.currentTimeMillis() - start;

                System.out.println("Thread " + threadNumber + " finished.");
            } finally {
                if (elapsedTimeMillis == 0) {
                    elapsedTimeMillis = System.currentTimeMillis() - start;
                }

                reportDone();

                try {
                    threadStmt.close();
                    threadConn.close();
                } catch (SQLException ex) {
                    // ignore
                }
            }
        }
    }

    class CreateThread extends Thread {
        BusyThread busyThread;
        int numConnections = 15;

        public CreateThread() {
        }

        public CreateThread(BusyThread toStop) {
            busyThread = toStop;
        }

        public CreateThread(int numConns) {
            numConnections = numConns;
        }

        public void run() {
            try {
                Connection[] connList = new Connection[numConnections];

                long maxConnTime = Long.MIN_VALUE;
                long minConnTime = Long.MAX_VALUE;
                double averageTime = 0;

                Properties nullProps = new Properties();

                for (int i = 0; i < numConnections; i++) {
                    long startTime = System.currentTimeMillis();
                    connList[i] = getConnectionWithProps(nullProps);

                    long endTime = System.currentTimeMillis();
                    long ellapsedTime = endTime - startTime;

                    if (ellapsedTime < minConnTime) {
                        minConnTime = ellapsedTime;
                    }

                    if (ellapsedTime > maxConnTime) {
                        maxConnTime = ellapsedTime;
                    }

                    averageTime += ((double) ellapsedTime / numConnections);
                }

                if (busyThread != null) {
                    busyThread.stop = true;
                }

                for (int i = 0; i < numConnections; i++) {
                    connList[i].close();
                }

                System.out.println(minConnTime + "/" + maxConnTime + "/"
                    + averageTime);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
