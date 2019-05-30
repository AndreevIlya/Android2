package ru.homecatering;

import android.os.AsyncTask;

class AsyncTaskMaker {
    private OnTaskListener listener;

    AsyncTaskMaker(OnTaskListener listener) {
        this.listener = listener;
    }

    public interface OnTaskListener {
        void onStart();

        void onStatusProgress(String values);

        void onComplete();
    }

    void doWork(Integer num) {
        new CustomAsyncTask(listener).execute(num);
    }

    private static class CustomAsyncTask extends AsyncTask<Integer, String, String> {

        private OnTaskListener listener;

        CustomAsyncTask(OnTaskListener listener) {
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {
            listener.onStart();
        }

        @Override
        protected void onPostExecute(String s) {
            listener.onComplete();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            listener.onStatusProgress(values[0]);
        }

        @Override
        protected String doInBackground(Integer... integers) {
            float[] arr = fillArray(integers[0]);
            for (int i = 0; i < arr.length; i++) {
                if (i == arr.length / 16) publishProgress("Just begun.");
                else if (i == arr.length / 8) publishProgress("Running at full speed.");
                else if (i == arr.length / 2) publishProgress("More than a half done.");
                else if (i == arr.length / 8 * 7) publishProgress("Nearly finished.");
                arr[i] = (float) (arr[i] * Math.sin(0.2f + (double) i / 5) * Math.cos(0.2f + (double) i / 5) * Math.cos(0.4f + (double) i / 2));
            }
            return null;
        }

        private float[] fillArray(int number) {
            float[] arr = new float[(int) Math.pow(2, number)];
            for (int i = 0; i < arr.length; i++) {
                arr[i] = 1;
            }
            return arr;
        }
    }
}
