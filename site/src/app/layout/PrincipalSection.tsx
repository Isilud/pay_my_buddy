import { PropsWithChildren } from "react";
import "./PrincipalSection.scss";

export function PrincipalSection(props: PropsWithChildren): JSX.Element {
  return (
    <section className="principalsection">
      <span>Breadcrumb</span>
      <div className="principalsection__content">{props.children}</div>
    </section>
  );
}
