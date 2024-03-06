package com.databasecourse.erfan.web.controller;

import com.databasecourse.erfan.web.util.CheckIfAdmin;
import com.sun.management.OperatingSystemMXBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.lang.management.ManagementFactory;

@Controller
public class ServerMetricsController {

    @GetMapping("/serverload")
    public ModelAndView showServerMetrics() {

        if (CheckIfAdmin.isAdmin()){
            ModelAndView modelandview = new ModelAndView("adminServerLoad");

            modelandview.addObject("pageTitle", "Server Load");



//            // Get memory usage
//            OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
//            long totalMemory = osBean.getTotalPhysicalMemorySize();
//            long freeMemory = osBean.getFreePhysicalMemorySize();
//            long usedMemory = totalMemory - freeMemory;
//
//            model.addObject("totalMemory", formatBytes(totalMemory));
//            model.addObject("usedMemory", formatBytes(usedMemory));
//            model.addObject("freeMemory", formatBytes(freeMemory));
//
//            // Get CPU usage
//            double cpuUsage = osBean.getSystemCpuLoad() * 100;
//
//            model.addObject("cpuUsage", cpuUsage);
//
//            // Get disk usage
//            File disk = new File("/");
//            long totalSpace = disk.getTotalSpace();
//            long freeSpace = disk.getFreeSpace();
//            long usedSpace = totalSpace - freeSpace;
//
//            model.addObject("totalSpace", formatBytes(totalSpace));
//            model.addObject("usedSpace", formatBytes(usedSpace));
//            model.addObject("freeSpace", formatBytes(freeSpace));
//

            return modelandview;
        } else {
            return null;
        }
    }

    @GetMapping("/metrics-data")
    @ResponseBody
    public MetricsData getMetricsData() {
        // Get memory usage
        OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        long totalMemory = osBean.getTotalPhysicalMemorySize();
        long freeMemory = osBean.getFreePhysicalMemorySize();
        long usedMemory = totalMemory - freeMemory;

        // Get CPU usage
        double cpuUsage = osBean.getSystemCpuLoad() * 100;

        // Get disk usage
        File disk = new File("/");
        long totalSpace = disk.getTotalSpace();
        long freeSpace = disk.getFreeSpace();
        long usedSpace = totalSpace - freeSpace;


        MetricsData md = new MetricsData(bytesToGigabytes(usedMemory), bytesToGigabytes(freeMemory), cpuUsage, bytesToGigabytes(usedSpace), bytesToGigabytes(freeSpace));
//        System.out.println("---- metric data ----");
//        System.out.println(md.toString());
        return md;
    }

    private static String formatBytes(long bytes) {
        final long kilo = 1024;
        final long mega = kilo * kilo;
        final long giga = kilo * kilo * kilo;

        if (bytes >= giga) {
            return String.format("%.2f GB", (double) bytes / giga);
        } else if (bytes >= mega) {
            return String.format("%.2f MB", (double) bytes / mega);
        } else if (bytes >= kilo) {
            return String.format("%.2f KB", (double) bytes / kilo);
        } else {
            return bytes + " B";
        }
    }





    private static class MetricsData {
        private final double usedMemory;
        private final double freeMemory;
        private final double cpuUsage;
        private final double usedSpace;
        private final double freeSpace;

        public MetricsData(double usedMemory, double freeMemory, double cpuUsage, double usedSpace, double freeSpace) {
            this.usedMemory = usedMemory;
            this.freeMemory = freeMemory;
            this.cpuUsage = cpuUsage;
            this.usedSpace = usedSpace;
            this.freeSpace = freeSpace;
        }

        public double getUsedMemory() {
            return usedMemory;
        }

        public double getFreeMemory() {
            return freeMemory;
        }

        public double getCpuUsage() {
            return cpuUsage;
        }

        public double getUsedSpace() {
            return usedSpace;
        }

        public double getFreeSpace() {
            return freeSpace;
        }

        @Override
        public String toString() {
            return "MetricsData{" +
                    "usedMemory=" + usedMemory +
                    ", freeMemory=" + freeMemory +
                    ", cpuUsage=" + cpuUsage +
                    ", usedSpace=" + usedSpace +
                    ", freeSpace=" + freeSpace +
                    '}';
        }
    }


    public static double bytesToGigabytes(long bytes) {
        double gigabytes = bytes / (1024.0 * 1024.0 * 1024.0);
        return gigabytes;
    }
}
