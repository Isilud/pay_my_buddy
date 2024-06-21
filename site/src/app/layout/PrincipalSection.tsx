import { PropsWithChildren } from "react";
import "./PrincipalSection.scss";
import Breadcrumb from "../component/Breadcrumb";
import { useLocation } from "react-router-dom";

export function PrincipalSection(props: PropsWithChildren): JSX.Element {
  return (
    <section className="principalsection">
      <Breadcrumb />
      <div className="principalsection__content">{props.children}</div>
    </section>
  );
}
