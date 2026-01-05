package interfaces;

import models.Task;

public interface Completable {
    void markAsComplete(Task task);
}
