package oracle.r2dbc;

import io.r2dbc.spi.R2dbcException;
import io.r2dbc.spi.Result;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * <p>
 * A subtype of the {@link Result.Segment} interface that provides information
 * on warnings raised by Oracle Database.
 * </p><p>
 * When a SQL command results in a warning, Oracle R2DBC emits a {@link Result}
 * with an {@code OracleR2dbcWarning} segment in addition to any other segments
 * that resulted from the SQL command. For example, if a SQL {@code SELECT}
 * command results in a warning, then an {@code OracleR2dbcWarning} segment is
 * included with the result, along with any {@link Result.RowSegment}s returned
 * by the {@code SELECT}.
 * </p><p>
 * R2DBC drivers typically emit {@code onError} signals for {@code Message}
 * segments that are not consumed by {@link Result#filter(Predicate)} or
 * {@link Result#flatMap(Function)}. Oracle R2DBC does not apply this behavior
 * for warning messages. If an {@code OracleR2dbcWarning}
 * segment is not consumed by the {@code filter} or {@code flatMap} methods of
 * a {@code Result}, then the warning is discarded and the result may be
 * consumed as normal with with the {@code map} or {@code getRowsUpdated}
 * methods.
 * </p><p>
 * Warning messages may be consumed with {@link Result#flatMap(Function)}:
 * </p><pre>{@code
 * result.flatMap(segment -> {
 *   if (segment instanceof OracleR2dbcWarning) {
 *     logWarning(((OracleR2dbcWarning)segment).getMessage());
 *     return emptyPublisher();
 *   }
 *   else {
 *     ... handle other segment types ...
 *   }
 * })
 * }</pre><p>
 * A {@code flatMap} function may also be used to convert a warning into an
 * {@code onError} signal:
 * </p><pre>{@code
 * result.flatMap(segment -> {
 *   if (segment instanceof OracleR2dbcWarning) {
 *     return errorPublisher(((OracleR2dbcWarning)segment).warning());
 *   }
 *   else {
 *     ... handle other segment types ...
 *   }
 * })
 * }</pre>
 * @since 1.1.0
 */
public interface OracleR2dbcWarning extends Result.Segment {

  /**
   * Returns the warning as an {@link R2dbcException}.
   * @return The warning as an {@link R2dbcException}. Not null.
   */
  R2dbcException exception();

  /**
   * Returns the error code of the warning.
   * @return The error code of the warning.
   */
  int errorCode();

  /**
   * Returns the SQLState of the warning.
   * @return The SQLState of the warning. Not null.
   */
  String sqlState();

  /**
   * Returns the text of the warning message.
   * @return The text of the warning message. Not null.
   */
  String message();
}
