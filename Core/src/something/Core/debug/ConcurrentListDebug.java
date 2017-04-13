package something.Core.debug;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

public class ConcurrentListDebug {
    public static <E> List<E> getProxy(List<E> source) {
        Object proxy = Proxy.newProxyInstance(ConcurrentListDebug.class.getClassLoader(), new Class[]{List.class}, new DebugInvocationHandler(source));
        //noinspection unchecked
        return (List<E>) proxy;
    }

    static class DebugInvocationHandler implements InvocationHandler {
        private boolean isIterated = false;
        private final List list;

        DebugInvocationHandler(List list) {
            this.list = list;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (isIterated) {
                throw new ConcurrentModificationException("DEBUG: " + method);
            }
            switch (method.getName()) {
                case "iterator":
                    isIterated = true;
                    Iterator iterator = (Iterator) method.invoke(list, args);
                    return new Iterator() {
                        @Override
                        public boolean hasNext() {
                            boolean b = iterator.hasNext();
                            if (!b) isIterated = false;
                            return b;
                        }
                        @Override
                        public Object next() {
                            return iterator.next();
                        }
                    };
                default:
                    return method.invoke(list, args);
            }
        }
    }
}
